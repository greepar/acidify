package org.ntqqrev.acidify.internal.packet.oidb

import org.ntqqrev.acidify.internal.protobuf.PbInt64
import org.ntqqrev.acidify.internal.protobuf.PbSchema
import org.ntqqrev.acidify.internal.protobuf.PbString

internal object FetchClientKeyResp : PbSchema() {
    val clientKey = PbString[3]
    val expireTime = PbInt64[4]
}