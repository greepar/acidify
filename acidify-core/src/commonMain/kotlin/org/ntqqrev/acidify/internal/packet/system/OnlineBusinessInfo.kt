package org.ntqqrev.acidify.internal.packet.system

import org.ntqqrev.acidify.internal.protobuf.PbInt32
import org.ntqqrev.acidify.internal.protobuf.PbSchema

internal object OnlineBusinessInfo : PbSchema() {
    val notifySwitch = PbInt32[1]
    val bindUinNotifySwitch = PbInt32[2]
}