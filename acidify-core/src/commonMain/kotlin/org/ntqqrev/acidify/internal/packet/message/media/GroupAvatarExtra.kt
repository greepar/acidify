package org.ntqqrev.acidify.internal.packet.message.media

import org.ntqqrev.acidify.internal.protobuf.PbInt32
import org.ntqqrev.acidify.internal.protobuf.PbInt64
import org.ntqqrev.acidify.internal.protobuf.PbSchema
import org.ntqqrev.acidify.internal.protobuf.get

internal object GroupAvatarExtra : PbSchema() {
    val type = PbInt32[1]           // 101
    val groupUin = PbInt64[2]       // 群号
    val field3 = GroupAvatarExtraField3[3]
    val field5 = PbInt32[5]         // 3
    val field6 = PbInt32[6]         // 1
}

internal object GroupAvatarExtraField3 : PbSchema() {
    val field1 = PbInt32[1]         // 1
}