package org.ntqqrev.acidify.internal.packet.oidb

import org.ntqqrev.acidify.internal.protobuf.PbSchema
import org.ntqqrev.acidify.internal.protobuf.PbString

internal object SetFilteredFriendRequestReq : PbSchema() {
    val selfUid = PbString[1]
    val requestUid = PbString[2]
}
