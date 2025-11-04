package org.ntqqrev.yogurt.util

actual fun readByteArrayFromFilePath(path: String): ByteArray {
    return java.io.File(path).readBytes()
}