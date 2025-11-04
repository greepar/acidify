package org.ntqqrev.acidify.internal.packet.message.extra

import org.ntqqrev.acidify.internal.protobuf.PbInt32
import org.ntqqrev.acidify.internal.protobuf.PbInt64
import org.ntqqrev.acidify.internal.protobuf.PbSchema
import org.ntqqrev.acidify.internal.protobuf.PbString

internal object SourceMsgResvAttr : PbSchema() {
    val oriMsgType = PbInt32[1]
    val sourceMsgId = PbInt64[2]
    val senderUid = PbString[3]
}