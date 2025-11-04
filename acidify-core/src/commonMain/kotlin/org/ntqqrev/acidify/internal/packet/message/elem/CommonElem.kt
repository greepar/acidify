package org.ntqqrev.acidify.internal.packet.message.elem

import org.ntqqrev.acidify.internal.protobuf.PbBytes
import org.ntqqrev.acidify.internal.protobuf.PbInt32
import org.ntqqrev.acidify.internal.protobuf.PbSchema

internal object CommonElem : PbSchema() {
    val serviceType = PbInt32[1]
    val pbElem = PbBytes[2]
    val businessType = PbInt32[3]
}