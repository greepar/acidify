package org.ntqqrev.acidify.internal.packet.message

import org.ntqqrev.acidify.internal.protobuf.PbSchema
import org.ntqqrev.acidify.internal.protobuf.get

internal object PushMsg : PbSchema() {
    val message = CommonMessage[1]
}