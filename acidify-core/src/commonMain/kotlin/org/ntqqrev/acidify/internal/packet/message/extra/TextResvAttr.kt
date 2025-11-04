package org.ntqqrev.acidify.internal.packet.message.extra

import org.ntqqrev.acidify.internal.protobuf.PbInt32
import org.ntqqrev.acidify.internal.protobuf.PbInt64
import org.ntqqrev.acidify.internal.protobuf.PbSchema
import org.ntqqrev.acidify.internal.protobuf.PbString

internal object TextResvAttr : PbSchema() {
    val atType = PbInt32[3] // 1 for @all, 2 for @specific
    val atMemberUin = PbInt64[4]
    val atMemberTinyid = PbInt64[5]
    val atMemberUid = PbString[9]
}