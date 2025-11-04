package org.ntqqrev.acidify.internal.packet.oidb

import org.ntqqrev.acidify.internal.protobuf.PbInt32
import org.ntqqrev.acidify.internal.protobuf.PbInt64
import org.ntqqrev.acidify.internal.protobuf.PbSchema
import org.ntqqrev.acidify.internal.protobuf.PbString

internal object SetMemberMuteReq : PbSchema() {
    val groupCode = PbInt64[1]
    val type = PbInt32[2]
    val targetUid = PbString[3]
    val duration = PbInt32[4]
}

