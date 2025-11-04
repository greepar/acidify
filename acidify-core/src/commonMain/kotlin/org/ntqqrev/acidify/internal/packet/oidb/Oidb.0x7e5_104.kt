package org.ntqqrev.acidify.internal.packet.oidb

import org.ntqqrev.acidify.internal.protobuf.PbInt32
import org.ntqqrev.acidify.internal.protobuf.PbSchema
import org.ntqqrev.acidify.internal.protobuf.PbString

internal object ProfileLikeReq : PbSchema() {
    val targetUid = PbString[11]
    val field2 = PbInt32[12]
    val field3 = PbInt32[13]
}