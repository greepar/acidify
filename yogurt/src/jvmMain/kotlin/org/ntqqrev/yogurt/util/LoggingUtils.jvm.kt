package org.ntqqrev.yogurt.util

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.AppenderBase
import io.ktor.util.logging.*
import org.ntqqrev.acidify.logging.LogHandler
import org.ntqqrev.acidify.logging.LogLevel
import org.ntqqrev.acidify.logging.shortenPackageName
import org.ntqqrev.yogurt.YogurtApp
import org.ntqqrev.yogurt.YogurtApp.t

const val internalLogTag = "Core"

private fun Logger.toAcidifyLogHandler(): LogHandler =
    LogHandler { level, tag, message, throwable ->
        val trueMessage = "${shortenPackageName(tag)}|$message"
        when (level) {
            LogLevel.VERBOSE -> trace(trueMessage)
            LogLevel.DEBUG -> debug(trueMessage)
            LogLevel.INFO -> info(trueMessage)
            LogLevel.WARN -> if (throwable == null) {
                warn(trueMessage)
            } else {
                warn(trueMessage, throwable)
            }

            LogLevel.ERROR -> if (throwable == null) {
                error(trueMessage)
            } else {
                error(trueMessage, throwable)
            }
        }
    }

class YogurtConsoleAppender : AppenderBase<ILoggingEvent>() {
    override fun append(eventObject: ILoggingEvent) {
        val (actualTag, actualMessage) = if (eventObject.loggerName == internalLogTag) {
            val splitIndex = eventObject.formattedMessage.indexOf('|')
            if (splitIndex != -1) {
                val tag = eventObject.formattedMessage.substring(0, splitIndex)
                val message = eventObject.formattedMessage.substring(splitIndex + 1)
                tag to message
            } else {
                eventObject.loggerName to eventObject.formattedMessage
            }
        } else {
            eventObject.loggerName to eventObject.formattedMessage
        }
        formatColoredLog(
            level = when (eventObject.level.toInt()) {
                Level.TRACE_INT -> LogLevel.VERBOSE
                Level.DEBUG_INT -> LogLevel.DEBUG
                Level.INFO_INT -> LogLevel.INFO
                Level.WARN_INT -> LogLevel.WARN
                Level.ERROR_INT -> LogLevel.ERROR
                else -> LogLevel.INFO
            },
            tag = actualTag,
            message = actualMessage,
            stackTrace = eventObject.throwableProxy?.let {
                buildString {
                    append(it.className)
                    append(": ")
                    append(it.message)
                    append("\n")
                    it.stackTraceElementProxyArray.forEach {
                        append("\t")
                        append(it)
                        append("\n")
                    }
                }
            }
        ).let(t::println)
    }
}

actual val YogurtApp.logHandler: LogHandler by lazy {
    KtorSimpleLogger(internalLogTag).toAcidifyLogHandler()
}