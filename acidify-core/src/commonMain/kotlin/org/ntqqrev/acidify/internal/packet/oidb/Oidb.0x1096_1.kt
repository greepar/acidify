package org.ntqqrev.acidify.internal.packet.oidb

import org.ntqqrev.acidify.internal.protobuf.PbBoolean
import org.ntqqrev.acidify.internal.protobuf.PbInt64
import org.ntqqrev.acidify.internal.protobuf.PbSchema
import org.ntqqrev.acidify.internal.protobuf.PbString

internal object SetMemberAdminReq : PbSchema() {
    val groupCode = PbInt64[1]
    val targetUid = PbString[2]
    val isAdmin = PbBoolean[3]
}

