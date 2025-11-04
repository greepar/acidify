package org.ntqqrev.acidify.internal.protobuf

abstract class PbSchema

inline operator fun <S : PbSchema> S.invoke(block: S.(PbObject<S>) -> Unit) = PbObject(this, block)

operator fun <S : PbSchema> S.invoke(byteArray: ByteArray) = PbObject(this, byteArray)