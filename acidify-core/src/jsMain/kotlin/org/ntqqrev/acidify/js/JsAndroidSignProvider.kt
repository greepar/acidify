package org.ntqqrev.acidify.js

import org.ntqqrev.acidify.common.SignResult
import kotlin.js.Promise

@JsExport
@JsName("AndroidSignProvider")
@AcidifyJsWrapper
interface JsAndroidSignProvider {
    fun sign(
        uin: Long,
        cmd: String,
        buffer: ByteArray,
        guid: String,
        seq: Int,
        version: String,
        qua: String,
    ): Promise<SignResult>

    fun energy(
        uin: Long,
        data: String,
        guid: String,
        ver: String,
        version: String,
        qua: String,
    ): Promise<ByteArray>

    fun getDebugXwid(
        uin: Long,
        data: String,
        guid: String,
        version: String,
        qua: String,
    ): Promise<ByteArray>
}
