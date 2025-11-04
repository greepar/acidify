package org.ntqqrev.acidify.internal.packet.message.elem

import org.ntqqrev.acidify.internal.protobuf.PbBytes
import org.ntqqrev.acidify.internal.protobuf.PbInt32
import org.ntqqrev.acidify.internal.protobuf.PbSchema

internal object TransElem : PbSchema() {
    val elemType = PbInt32[1]
    val elemValue = PbBytes[2]
}