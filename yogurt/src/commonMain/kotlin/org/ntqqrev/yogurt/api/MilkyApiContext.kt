package org.ntqqrev.yogurt.api

import io.ktor.server.application.*
import org.ntqqrev.acidify.AbstractBot

class MilkyApiContext(
    val bot: AbstractBot,
    val application: Application,
)