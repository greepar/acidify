package org.ntqqrev.acidify.internal.packet.message.extra

import org.ntqqrev.acidify.internal.protobuf.PbOptional
import org.ntqqrev.acidify.internal.protobuf.PbSchema
import org.ntqqrev.acidify.internal.protobuf.PbString
import org.ntqqrev.acidify.internal.protobuf.get

internal object FriendRequestExtractVia : PbSchema() {
    val body = PbOptional[Body[1]]

    internal object Body : PbSchema() {
        val via = PbString[5]
    }
}