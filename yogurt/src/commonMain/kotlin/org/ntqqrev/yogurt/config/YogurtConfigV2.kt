@file:OptIn(ExperimentalSerializationApi::class)

package org.ntqqrev.yogurt.config

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable

@Serializable
class YogurtConfigV2(
    val configVersion: Int = 2,
    val signApiUrl: String = "",
    val protocol: YogurtConfig.Protocol = YogurtConfig.Protocol(),
    val androidCredentials: YogurtConfig.AndroidCredentials = YogurtConfig.AndroidCredentials(),
    val androidUseLegacySign: Boolean = false,
    val reportSelfMessage: Boolean = true,
    val preloadContacts: Boolean = false,
    val transformIncomingMFaceToImage: Boolean = false,
    val httpConfig: YogurtConfig.MilkyHttpConfig = YogurtConfig.MilkyHttpConfig(),
    val webhookConfig: List<WebhookEndpointConfig> = emptyList(),
    val logging: YogurtConfig.LoggingConfig = YogurtConfig.LoggingConfig(),
    val skipSecurityCheck: Boolean = false,
) {
    @Serializable
    class WebhookEndpointConfig(
        val url: String = "",
        val accessToken: String = "",
    )
}