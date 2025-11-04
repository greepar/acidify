package org.ntqqrev.acidify.internal.packet.message

import org.ntqqrev.acidify.internal.protobuf.PbSchema
import org.ntqqrev.acidify.internal.protobuf.get

internal object CommonMessage : PbSchema() {
    val routingHead = RoutingHead[1]
    val contentHead = ContentHead[2]
    val messageBody = MessageBody[3]
}