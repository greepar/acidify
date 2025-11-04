package org.ntqqrev.acidify.internal.packet.oidb

import org.ntqqrev.acidify.internal.protobuf.PbInt64
import org.ntqqrev.acidify.internal.protobuf.PbSchema
import org.ntqqrev.acidify.internal.protobuf.PbString

internal object SetGroupNameReq : PbSchema() {
    val groupCode = PbInt64[1]
    val targetName = PbString[2]
}

