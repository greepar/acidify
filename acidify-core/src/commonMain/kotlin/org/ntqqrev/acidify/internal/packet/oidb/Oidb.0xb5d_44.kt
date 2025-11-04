package org.ntqqrev.acidify.internal.packet.oidb

import org.ntqqrev.acidify.internal.protobuf.PbInt32
import org.ntqqrev.acidify.internal.protobuf.PbSchema
import org.ntqqrev.acidify.internal.protobuf.PbString

internal object SetFriendRequestReq : PbSchema() {
    val accept = PbInt32[1]
    val targetUid = PbString[2]
}