@file:Suppress("duplicatedCode")

package org.ntqqrev.yogurt.util

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.auth.*
import io.ktor.client.plugins.auth.providers.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.ntqqrev.acidify.common.SignResult
import org.ntqqrev.acidify.common.android.AndroidSignProvider
import org.ntqqrev.acidify.exception.UrlSignException

data class LegacySignMetainfo(
    val fullVersion: String,
    val fekitVersion: String,
)

val legacyMetainfoMap = mapOf(
    "9.1.60" to LegacySignMetainfo(
        fullVersion = "9.1.60.24370",
        fekitVersion = "8.400.807",
    ),
    "9.1.70" to LegacySignMetainfo(
        fullVersion = "9.1.70.25645",
        fekitVersion = "8.401.830",
    ),
    "9.2.0" to LegacySignMetainfo(
        fullVersion = "9.2.0.28325",
        fekitVersion = "8.405.873",
    ),
    "9.2.20" to LegacySignMetainfo(
        fullVersion = "9.2.20.30025",
        fekitVersion = "8.409.903",
    ),
)

class AndroidLegacyUrlSignProvider(val url: String) : AndroidSignProvider {
    val base = Url(url)

    private val client = HttpClient {
        if (!(base.user.isNullOrEmpty() || base.password.isNullOrEmpty())) {
            install(Auth) {
                basic {
                    credentials {
                        BasicAuthCredentials(
                            username = base.user!!,
                            password = base.password!!
                        )
                    }
                }
            }
        }
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    override suspend fun sign(
        uin: Long,
        cmd: String,
        buffer: ByteArray,
        guid: String,
        seq: Int,
        version: String,
        qua: String
    ): SignResult {
        val resp = client.post {
            url {
                takeFrom(base)
                appendPathSegments("sign")
            }
            contentType(ContentType.Application.Json)
            setBody(
                AndroidUrlSignRequest(
                    uin = uin,
                    cmd = cmd,
                    buffer = buffer.toHexString(),
                    guid = guid,
                    seq = seq,
                    version = version,
                    qua = qua,
                )
            )
        }
        if (resp.status != HttpStatusCode.OK) {
            throw UrlSignException(resp.status.description, resp.status.value)
        }
        val respBody = resp.body<AndroidUrlSignResponse<AndroidUrlSignValue>>()
        if (respBody.code != 0 || respBody.data == null) {
            throw UrlSignException(respBody.msg, respBody.code)
        }
        return SignResult(
            sign = respBody.data.sign.hexToByteArray(),
            token = respBody.data.token.hexToByteArray(),
            extra = respBody.data.extra.hexToByteArray(),
        )
    }

    override suspend fun energy(
        uin: Long,
        data: String,
        guid: String,
        ver: String,
        version: String,
        qua: String
    ): ByteArray {
        val resp = client.post {
            url {
                takeFrom(base)
                appendPathSegments("energy")
            }
            contentType(ContentType.Application.Json)
            setBody(
                AndroidUrlEnergyRequest(
                    uin = uin,
                    data = data,
                    guid = guid,
                    ver = ver,
                    version = version,
                    qua = qua,
                )
            )
        }
        if (resp.status != HttpStatusCode.OK) {
            throw UrlSignException(resp.status.description, resp.status.value)
        }
        val respBody = resp.body<AndroidUrlSignResponse<String>>()
        if (respBody.code != 0 || respBody.data == null) {
            throw UrlSignException(respBody.msg, respBody.code)
        }
        return respBody.data.hexToByteArray()
    }

    override suspend fun getDebugXwid(
        uin: Long,
        data: String,
        guid: String,
        version: String,
        qua: String
    ): ByteArray =
        "0A93010A9001413441393532384243323537343030303644314445303543333538313245363041454239333935424444364433323731454644454542394634304536383734313141444439393835353341364436414132413433363637373839463746424236333442443444443031443436353735323238373339393032373836443445384333313241443631444346424537413246"
            .hexToByteArray()

    suspend fun registerDevice(
        ver: String,
        fekitVer: String,
        uin: Long,
        qimei: String,
        guid: String,
    ) {
        val resp = client.post {
            url {
                takeFrom(base)
                appendPathSegments("register")
            }
            contentType(ContentType.Application.Json)
            setBody(
                AndroidUrlRegisterRequest(
                    ver = ver,
                    fekitVer = fekitVer,
                    uin = uin,
                    androidId = "d4573bde6663bb55", // hardcoded
                    qimei36 = qimei,
                    guid = guid,
                )
            )
        }
        if (resp.status != HttpStatusCode.OK) {
            throw UrlSignException(resp.status.description, resp.status.value)
        }
        val respBody = resp.body<AndroidUrlSignResponse<JsonElement>>()
        if (respBody.code != 0) {
            throw UrlSignException(respBody.msg, respBody.code)
        }
    }

    suspend fun registerDeviceBySign(
        qua: String,
        uin: Long,
        qimei: String,
        guid: String,
    ) {
        val resp = client.post {
            url {
                takeFrom(base)
                appendPathSegments("sign")
            }
            contentType(ContentType.Application.Json)
            setBody(
                buildJsonObject {
                    put("qua", qua)
                    put("uin", uin)
                    put("cmd", "trpc.login.ecdh.EcdhService.SsoKeyExchange") // arbitrary valid cmd
                    put("seq", 28655) // arbitrary seq
                    put("android_id", "d4573bde6663bb55")
                    put("qimei36", qimei)
                    put("buffer", "00aaff00aaff00aaff") // arbitrary buffer
                    put("guid", guid)
                }
            )
        }
        if (resp.status != HttpStatusCode.OK) {
            throw UrlSignException(resp.status.description, resp.status.value)
        }
        val respBody = resp.body<AndroidUrlSignResponse<AndroidUrlSignValue>>()
        if (respBody.code != 0) {
            throw UrlSignException(respBody.msg, respBody.code)
        }
        // Sign successful, but we don't actually care about the returned sign value
    }
}

@Serializable
private data class AndroidUrlSignRequest(
    val uin: Long,
    val cmd: String,
    val buffer: String,
    val guid: String,
    val seq: Int,
    val version: String,
    val qua: String
)

@Serializable
private data class AndroidUrlEnergyRequest(
    val uin: Long,
    val data: String,
    val guid: String,
    val ver: String,
    val version: String,
    val qua: String
)

@Serializable
private data class AndroidUrlRegisterRequest(
    val ver: String,
    @SerialName("fekit_ver") val fekitVer: String,
    val uin: Long,
    @SerialName("android_id") val androidId: String,
    val qimei36: String,
    val guid: String
)

@Serializable
private data class AndroidUrlSignResponse<T>(
    val code: Int = 0,
    val msg: String = "",
    val data: T? = null,
)

@Serializable
private data class AndroidUrlSignValue(
    val sign: String,
    val token: String,
    val extra: String
)