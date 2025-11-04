package org.ntqqrev.acidify.internal.packet.oidb

import org.ntqqrev.acidify.internal.protobuf.PbInt64
import org.ntqqrev.acidify.internal.protobuf.PbSchema

internal object QuitGroupReq : PbSchema() {
    val groupCode = PbInt64[1]
}

