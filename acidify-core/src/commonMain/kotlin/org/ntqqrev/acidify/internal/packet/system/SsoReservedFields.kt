package org.ntqqrev.acidify.internal.packet.system

import org.ntqqrev.acidify.internal.protobuf.PbOptional
import org.ntqqrev.acidify.internal.protobuf.PbSchema
import org.ntqqrev.acidify.internal.protobuf.PbString
import org.ntqqrev.acidify.internal.protobuf.get

internal object SsoReservedFields : PbSchema() {
    val trace = PbString[15]
    val uid = PbOptional[PbString[16]]
    val secureInfo = PbOptional[SsoSecureInfo[24]]
}