@file:OptIn(ExperimentalSerializationApi::class)

package org.ntqqrev.yogurt.util

import kotlinx.io.buffered
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.*
import kotlinx.serialization.json.io.encodeToSink
import org.ntqqrev.yogurt.config.YogurtConfig
import org.ntqqrev.yogurt.config.YogurtConfigV2
import org.ntqqrev.yogurt.configPath

fun YogurtConfig.toV2() = YogurtConfigV2(
    signApiUrl = signApiUrl,
    protocol = protocol,
    androidCredentials = androidCredentials,
    androidUseLegacySign = androidUseLegacySign,
    reportSelfMessage = reportSelfMessage,
    preloadContacts = preloadContacts,
    transformIncomingMFaceToImage = transformIncomingMFaceToImage,
    httpConfig = httpConfig,
    webhookConfig = webhookConfig.url.map { webhookUrl ->
        YogurtConfigV2.WebhookEndpointConfig(
            url = webhookUrl,
            accessToken = webhookConfig.accessToken,
        )
    },
    logging = logging,
    skipSecurityCheck = skipSecurityCheck,
)

fun detectConfigVersion(configJson: JsonElement): Int {
    return configJson.jsonObject["configVersion"]?.jsonPrimitive?.intOrNull ?: 1
}

val jsonModule = Json {
    prettyPrint = true
    encodeDefaults = true
    allowComments = true
    allowTrailingComma = true
    ignoreUnknownKeys = true
}

fun loadConfigAndUpdate(): YogurtConfigV2 {
    if (!SystemFileSystem.exists(configPath)) {
        val defaultConfig = YogurtConfigV2()
        SystemFileSystem.sink(configPath).buffered().use {
            jsonModule.encodeToSink(defaultConfig, it)
        }
        println("配置文件已生成于 ${SystemFileSystem.resolve(configPath)}")
        println("请根据需要进行修改，修改完成后按 Enter 键继续...")
        readln()
    }

    val configJson = jsonModule.parseToJsonElement(
        SystemFileSystem.source(configPath).buffered().use {
            it.readString()
        }
    )
    val config = when (val configVersion = detectConfigVersion(configJson)) {
        1 -> jsonModule.decodeFromJsonElement<YogurtConfig>(configJson).toV2()
        2 -> jsonModule.decodeFromJsonElement<YogurtConfigV2>(configJson)
        else -> error("不支持的配置版本 $configVersion，当前仅支持 V1 和 V${2}")
    }

    SystemFileSystem.sink(configPath).buffered().use { sink ->
        jsonModule.encodeToSink(config, sink)
        // rewrite it to format, and add new fields if any
    }

    return config
}