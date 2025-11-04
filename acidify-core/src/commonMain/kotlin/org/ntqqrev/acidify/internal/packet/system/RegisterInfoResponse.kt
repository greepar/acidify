package org.ntqqrev.acidify.internal.packet.system

import org.ntqqrev.acidify.internal.protobuf.PbSchema
import org.ntqqrev.acidify.internal.protobuf.PbString

internal object RegisterInfoResponse : PbSchema() {
    val message = PbString[2]
}