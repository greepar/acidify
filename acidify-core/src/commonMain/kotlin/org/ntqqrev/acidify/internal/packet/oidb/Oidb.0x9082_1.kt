package org.ntqqrev.acidify.internal.packet.oidb

import org.ntqqrev.acidify.internal.protobuf.PbInt32
import org.ntqqrev.acidify.internal.protobuf.PbInt64
import org.ntqqrev.acidify.internal.protobuf.PbSchema
import org.ntqqrev.acidify.internal.protobuf.PbString

internal object SetGroupMessageReactionReq : PbSchema() {
    val groupCode = PbInt64[2]
    val sequence = PbInt64[3]
    val code = PbString[4]
    val type = PbInt32[5]
}

