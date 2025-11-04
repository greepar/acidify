package org.ntqqrev.acidify.internal.packet.message.elem

import org.ntqqrev.acidify.internal.protobuf.PbInt32
import org.ntqqrev.acidify.internal.protobuf.PbSchema

internal object Face : PbSchema() {
    val index = PbInt32[1]
}