package org.ntqqrev.acidify.internal.packet.message

import org.ntqqrev.acidify.internal.protobuf.PbBytes
import org.ntqqrev.acidify.internal.protobuf.PbInt32
import org.ntqqrev.acidify.internal.protobuf.PbSchema

internal object Trans211TmpMsg : PbSchema() {
    val msgBody = PbBytes[1]
    val c2CCmd = PbInt32[2]
}