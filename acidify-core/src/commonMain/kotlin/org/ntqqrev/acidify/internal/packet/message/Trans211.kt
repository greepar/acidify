package org.ntqqrev.acidify.internal.packet.message

import org.ntqqrev.acidify.internal.protobuf.PbInt32
import org.ntqqrev.acidify.internal.protobuf.PbInt64
import org.ntqqrev.acidify.internal.protobuf.PbSchema
import org.ntqqrev.acidify.internal.protobuf.PbString

internal object Trans211 : PbSchema() {
    val toUin = PbInt64[1]
    val ccCmd = PbInt32[2]
    val uid = PbString[8]
}