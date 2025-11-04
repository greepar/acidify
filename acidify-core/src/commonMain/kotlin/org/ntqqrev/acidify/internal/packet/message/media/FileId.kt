package org.ntqqrev.acidify.internal.packet.message.media

import org.ntqqrev.acidify.internal.protobuf.PbInt32
import org.ntqqrev.acidify.internal.protobuf.PbSchema

internal object FileId : PbSchema() {
    val appId = PbInt32[4]
    val ttl = PbInt32[10]
}