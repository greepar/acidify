package org.ntqqrev.acidify.internal.packet.message.extra

import org.ntqqrev.acidify.internal.packet.message.NotOnlineFile
import org.ntqqrev.acidify.internal.protobuf.PbSchema
import org.ntqqrev.acidify.internal.protobuf.get

internal object PrivateFileExtra : PbSchema() {
    val notOnlineFile = NotOnlineFile[1]
}