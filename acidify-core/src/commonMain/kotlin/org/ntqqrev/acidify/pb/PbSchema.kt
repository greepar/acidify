package org.ntqqrev.acidify.pb

abstract class PbSchema

internal inline operator fun <S : PbSchema> S.invoke(block: S.(PbObject<S>) -> Unit) = PbObject(this, block)

internal operator fun <S : PbSchema> S.invoke(byteArray: ByteArray) = PbObject(this, byteArray)