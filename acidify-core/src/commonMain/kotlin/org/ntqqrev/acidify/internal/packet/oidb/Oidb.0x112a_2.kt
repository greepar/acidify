package org.ntqqrev.acidify.internal.packet.oidb

import org.ntqqrev.acidify.internal.protobuf.*

internal object SetUserProfileReq : PbSchema() {
    val uin = PbInt64[1]
    val stringProps = PbRepeated[StringProp[2]]
    val numberProps = PbRepeated[NumberProp[3]]

    internal object StringProp : PbSchema() {
        val key = PbInt32[1]
        val value = PbString[2]
    }

    internal object NumberProp : PbSchema() {
        val key = PbInt32[1]
        val value = PbInt32[2]
    }
}