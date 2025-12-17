package org.ntqqrev.acidify.internal.packet.oidb

import org.ntqqrev.acidify.internal.protobuf.*

internal object DeleteFriendReq : PbSchema() {
    val body = Body[1]

    object Body : PbSchema() {
        val targetUid = PbString[1]
        val field2 = Field2[2]
        val block = PbBoolean[3]
        val field4 = PbBoolean[4]

        object Field2 : PbSchema() {
            val field1 = PbInt32[1]
            val field2 = PbInt32[2]
            val field3 = Field3[3]

            object Field3 : PbSchema() {
                val field1 = PbInt32[1]
                val field2 = PbInt32[2]
                val field3 = PbInt32[3]
            }
        }
    }
}