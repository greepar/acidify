package org.ntqqrev.acidify.internal.packet.message.extra

import org.ntqqrev.acidify.internal.protobuf.PbInt32
import org.ntqqrev.acidify.internal.protobuf.PbSchema
import org.ntqqrev.acidify.internal.protobuf.PbString

internal object QBigFaceExtra : PbSchema() {
    val aniStickerPackId = PbString[1]
    val aniStickerId = PbString[2]
    val faceId = PbInt32[3]
    val field4 = PbInt32[4]
    val aniStickerType = PbInt32[5]
    val field6 = PbString[6]
    val preview = PbString[7]
    val field9 = PbInt32[9]
}