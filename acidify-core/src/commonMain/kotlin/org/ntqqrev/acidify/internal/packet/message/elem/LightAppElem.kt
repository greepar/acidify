package org.ntqqrev.acidify.internal.packet.message.elem

import org.ntqqrev.acidify.internal.protobuf.PbBytes
import org.ntqqrev.acidify.internal.protobuf.PbSchema

internal object LightAppElem : PbSchema() {
    val bytesData = PbBytes[1]
    val bytesMsgResid = PbBytes[2]
}