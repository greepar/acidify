package org.ntqqrev.acidify.internal.packet.system

import org.ntqqrev.acidify.internal.protobuf.PbSchema
import org.ntqqrev.acidify.internal.protobuf.get

internal object UnRegisterInfo : PbSchema() {
    val device = DeviceInfo[2]
}