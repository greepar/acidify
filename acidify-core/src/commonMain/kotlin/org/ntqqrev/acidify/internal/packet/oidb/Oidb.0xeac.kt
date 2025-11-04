package org.ntqqrev.acidify.internal.packet.oidb

import org.ntqqrev.acidify.internal.protobuf.PbInt32
import org.ntqqrev.acidify.internal.protobuf.PbInt64
import org.ntqqrev.acidify.internal.protobuf.PbSchema

internal object SetGroupEssenceMessageReq : PbSchema() {
    val groupCode = PbInt64[1]
    val sequence = PbInt64[2]
    val random = PbInt32[3]
}

