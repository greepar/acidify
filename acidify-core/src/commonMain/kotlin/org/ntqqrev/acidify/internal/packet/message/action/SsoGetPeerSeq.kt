package org.ntqqrev.acidify.internal.packet.message.action

import org.ntqqrev.acidify.internal.protobuf.PbInt64
import org.ntqqrev.acidify.internal.protobuf.PbSchema
import org.ntqqrev.acidify.internal.protobuf.PbString

internal object SsoGetPeerSeqReq : PbSchema() {
    val peerUid = PbString[1]
}

internal object SsoGetPeerSeqResp : PbSchema() {
    val seq1 = PbInt64[3]
    val seq2 = PbInt64[4]
    val latestMsgTime = PbInt64[5]
}

