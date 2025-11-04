package org.ntqqrev.acidify.internal.packet.message.extra

import org.ntqqrev.acidify.internal.protobuf.PbInt64
import org.ntqqrev.acidify.internal.protobuf.PbSchema
import org.ntqqrev.acidify.internal.protobuf.PbString

internal object GroupInvitation : PbSchema() {
    val groupUin = PbInt64[1]
    val invitorUid = PbString[5]
}

