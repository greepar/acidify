package org.ntqqrev.acidify.logging

fun interface LogHandler {
    fun handleLog(level: LogLevel, tag: String, message: String, throwable: Throwable?)
}