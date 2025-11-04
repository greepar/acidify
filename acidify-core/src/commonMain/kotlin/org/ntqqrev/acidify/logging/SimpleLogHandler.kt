package org.ntqqrev.acidify.logging

object SimpleLogHandler : LogHandler {
    override fun handleLog(
        level: LogLevel,
        tag: String,
        message: String,
        throwable: Throwable?
    ) {
        val logMessage = buildString {
            append("[${level.name}] ")
            append("[$tag] ")
            append(message)
            throwable?.let {
                append("\n")
                append(it.stackTraceToString())
            }
        }
        println(logMessage)
    }
}