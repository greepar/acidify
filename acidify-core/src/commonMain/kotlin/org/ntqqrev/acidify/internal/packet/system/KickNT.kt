package org.ntqqrev.acidify.internal.packet.system

import org.ntqqrev.acidify.internal.protobuf.PbSchema
import org.ntqqrev.acidify.internal.protobuf.PbString

internal object KickNT : PbSchema() {
    val tip = PbString[3]
    val title = PbString[4]
}