package org.ntqqrev.acidify.js

import kotlinx.coroutines.promise
import org.ntqqrev.acidify.common.SignResult
import org.ntqqrev.acidify.common.android.AndroidUrlSignProvider
import kotlin.js.Promise

@JsExport
@JsName("AndroidUrlSignProvider")
@AcidifyJsWrapper
class JsAndroidUrlSignProvider(
    val scope: JsCoroutineScope,
    url: String,
    httpProxy: String? = null
) : JsAndroidSignProvider {
    private val urlSignProvider = AndroidUrlSignProvider(url, httpProxy)

    override fun sign(
        uin: Long,
        cmd: String,
        buffer: ByteArray,
        guid: String,
        seq: Int,
        version: String,
        qua: String,
    ): Promise<SignResult> = scope.value.promise {
        urlSignProvider.sign(uin, cmd, buffer, guid, seq, version, qua)
    }

    override fun energy(
        uin: Long,
        data: String,
        guid: String,
        ver: String,
        version: String,
        qua: String,
    ): Promise<ByteArray> = scope.value.promise {
        urlSignProvider.energy(uin, data, guid, ver, version, qua)
    }

    override fun getDebugXwid(
        uin: Long,
        data: String,
        guid: String,
        version: String,
        qua: String,
    ): Promise<ByteArray> = scope.value.promise {
        urlSignProvider.getDebugXwid(uin, data, guid, version, qua)
    }
}
