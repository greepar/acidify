package org.ntqqrev.acidify.internal.context

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.sync.Semaphore
import kotlinx.io.buffered
import kotlinx.io.readTo
import org.ntqqrev.acidify.common.MediaSource
import org.ntqqrev.acidify.internal.AbstractClient
import org.ntqqrev.acidify.internal.crypto.hash.MD5Stream
import org.ntqqrev.acidify.internal.crypto.hash.SHA1Stream
import org.ntqqrev.acidify.internal.proto.message.media.FlashTransferSha1StateV
import org.ntqqrev.acidify.internal.proto.message.media.FlashTransferUploadBody
import org.ntqqrev.acidify.internal.proto.message.media.FlashTransferUploadReq
import org.ntqqrev.acidify.internal.proto.message.media.FlashTransferUploadResp
import org.ntqqrev.acidify.internal.util.pbDecode
import org.ntqqrev.acidify.internal.util.pbEncode
import org.ntqqrev.acidify.internal.util.sha1
import kotlin.time.TimeSource

internal class FlashTransferContext(client: AbstractClient) : AbstractContext(client) {
    private val httpClient = HttpClient()
    private val url = "https://multimedia.qfile.qq.com/sliceupload"
    private val uploadLimiter = Semaphore(MAX_CONCURRENT_UPLOADS)

    companion object {
        const val CHUNK_SIZE = 1024 * 1024 // 1MB
        const val MAX_CONCURRENT_UPLOADS = 10
        const val MAX_UPLOAD_RETRIES = 3
    }

    data class UploadMetadata(
        val size: Long,
        val md5: ByteArray,
        val sha1: ByteArray,
        val sha1StateList: List<ByteArray>,
    )

    fun prepareUpload(source: MediaSource): UploadMetadata {
        val startedAt = TimeSource.Monotonic.markNow()
        val size = source.size
        val chunkCount = ((size + CHUNK_SIZE - 1) / CHUNK_SIZE).toInt()
        val md5Stream = MD5Stream()
        val sha1Stream = SHA1Stream()
        val sha1StateList = mutableListOf<ByteArray>()
        var finalSha1: ByteArray? = null
        val scanSource = source.openRawSource().buffered()
        try {
            for (i in 0 until chunkCount) {
                val chunkSize = minOf(CHUNK_SIZE.toLong(), size - i.toLong() * CHUNK_SIZE).toInt()
                val chunkBuffer = ByteArray(chunkSize)
                scanSource.readTo(chunkBuffer)
                md5Stream.update(chunkBuffer)
                sha1Stream.update(chunkBuffer)
                val digest = ByteArray(SHA1Stream.Sha1DigestSize)
                if (i != chunkCount - 1) {
                    sha1Stream.hash(digest, false)
                } else {
                    sha1Stream.final(digest)
                    finalSha1 = digest
                }
                sha1StateList.add(digest)
            }
        } finally {
            scanSource.close()
        }

        val md5 = ByteArray(MD5Stream.Md5DigestSize)
        md5Stream.final(md5)
        val sha1 = finalSha1 ?: ByteArray(SHA1Stream.Sha1DigestSize).also { sha1Stream.final(it) }
        logger.d { "FlashTransfer 上传前校验完成，耗时 ${startedAt.elapsedNow()}" }
        return UploadMetadata(size, md5, sha1, sha1StateList)
    }

    suspend fun uploadFile(
        uKey: String,
        appId: Int,
        source: MediaSource,
        size: Long,
        sha1StateList: List<ByteArray>? = null,
    ): Boolean {
        val chunkCount = ((size + CHUNK_SIZE - 1) / CHUNK_SIZE).toInt()
        val uploadSha1StateList = sha1StateList ?: prepareUpload(source).sha1StateList

        return coroutineScope {
            if (chunkCount == 0) {
                return@coroutineScope true
            }

            val uploadSource = source.openRawSource().buffered()
            val uploadedBytes = atomic(0L)
            val uploadTasks = mutableListOf<Deferred<Boolean>>()
            try {
                val lastChunkIndex = chunkCount - 1
                for (i in 0 until lastChunkIndex) {
                    val chunkStart = i * CHUNK_SIZE
                    val chunkLength = minOf(CHUNK_SIZE.toLong(), size - chunkStart.toLong()).toInt()
                    val uploadBuffer = ByteArray(chunkLength)
                    uploadSource.readTo(uploadBuffer)

                    uploadLimiter.acquire()
                    uploadTasks += async {
                        try {
                            val success = uploadChunk(
                                uKey = uKey,
                                appId = appId,
                                start = chunkStart,
                                sha1StateList = uploadSha1StateList,
                                body = uploadBuffer
                            )
                            if (success) {
                                val progress = uploadedBytes.addAndGet(chunkLength.toLong()) * 100L / size
                                logger.d { "FlashTransfer 上传进度: $progress%" }
                            }
                            success
                        } finally {
                            uploadLimiter.release()
                        }
                    }
                }

                // 最后一片可能触发服务端合并，必须等前面的分片全部上传成功后再发。
                if (!uploadTasks.awaitAll().all { it }) {
                    return@coroutineScope false
                }

                val lastChunkStart = lastChunkIndex * CHUNK_SIZE
                val lastChunkLength = minOf(CHUNK_SIZE.toLong(), size - lastChunkStart.toLong()).toInt()
                val lastUploadBuffer = ByteArray(lastChunkLength)
                uploadSource.readTo(lastUploadBuffer)
                uploadLimiter.acquire()
                val success = try {
                    uploadChunk(
                        uKey = uKey,
                        appId = appId,
                        start = lastChunkStart,
                        sha1StateList = uploadSha1StateList,
                        body = lastUploadBuffer
                    )
                } finally {
                    uploadLimiter.release()
                }
                if (success) {
                    val progress = uploadedBytes.addAndGet(lastChunkLength.toLong()) * 100L / size
                    logger.d { "FlashTransfer 上传进度: $progress%" }
                }
                success
            } finally {
                uploadSource.close()
            }
        }
    }

    private suspend fun uploadChunk(
        uKey: String,
        appId: Int,
        start: Int,
        sha1StateList: List<ByteArray>,
        body: ByteArray
    ): Boolean {
        val chunkSha1 = body.sha1()
        val end = start + body.size - 1
        val req = FlashTransferUploadReq(
            field1 = 0,
            appId = appId,
            field3 = 2,
            body = FlashTransferUploadBody(
                field1 = ByteArray(0),
                uKey = uKey,
                start = start,
                end = end,
                sha1 = chunkSha1,
                sha1StateV = FlashTransferSha1StateV(
                    state = sha1StateList
                ),
                body = body,
            )
        )
        val payload = req.pbEncode()
        for (attempt in 1..MAX_UPLOAD_RETRIES) {
            try {
                val response = httpClient.post(url) {
                    headers {
                        append(HttpHeaders.Accept, "*/*")
                        append(HttpHeaders.Connection, "Keep-Alive")
                        append(HttpHeaders.AcceptEncoding, "gzip")
                    }
                    setBody(payload)
                }
                val responseBytes = response.readRawBytes()
                if (!response.status.isSuccess()) {
                    logger.w { "FlashTransfer 上传块 $start 第 $attempt/$MAX_UPLOAD_RETRIES 次失败: ${response.status}, ${responseBytes.toHexString()}" }
                } else {
                    val resp = responseBytes.pbDecode<FlashTransferUploadResp>()
                    val status = resp.status
                    if (status == "success") {
                        return true
                    }
                    logger.w { "FlashTransfer 上传块 $start 第 $attempt/$MAX_UPLOAD_RETRIES 次失败: $status" }
                }
            } catch (e: Exception) {
                if (attempt == MAX_UPLOAD_RETRIES) {
                    logger.e(e) { "FlashTransfer 上传块 $start 异常: ${e.message}" }
                } else {
                    logger.w(e) { "FlashTransfer 上传块 $start 第 $attempt/$MAX_UPLOAD_RETRIES 次异常，准备重试: ${e.message}" }
                }
            }
            if (attempt < MAX_UPLOAD_RETRIES) {
                delay(300L * attempt)
            }
        }
        return false
    }
}
