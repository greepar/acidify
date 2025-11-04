package org.ntqqrev.acidify.logging

object NopLogHandler : LogHandler {
    override fun handleLog(
        level: LogLevel,
        tag: String,
        message: String,
        throwable: Throwable?
    ) {
        // do nothing
    }
}