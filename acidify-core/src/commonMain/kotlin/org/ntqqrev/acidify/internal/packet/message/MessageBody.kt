package org.ntqqrev.acidify.internal.packet.message

import org.ntqqrev.acidify.internal.protobuf.PbBytes
import org.ntqqrev.acidify.internal.protobuf.PbSchema
import org.ntqqrev.acidify.internal.protobuf.get

internal object MessageBody : PbSchema() {
    val richText = RichText[1]
    val msgContent = PbBytes[2]
    val msgEncryptContent = PbBytes[3]
}