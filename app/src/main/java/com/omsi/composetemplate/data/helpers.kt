package com.omsi.composetemplate.data

import kotlin.experimental.and


fun bytesToInt(byte0:Byte, byte1:Byte):Int{
    //Integer in PLC is Short in Java:
    //positive: 0x0000..0x7FFF
    //negative: 0x8000..0xFFFF
    val short:Short = (byte1.toUInt().shl(8) or (byte0.toUInt() and 255u)).toShort()
    return (short.toInt())
}


fun getBitValue(byte0:Byte, position:Int):Int{
    val n = position.apply { position.coerceAtLeast(0).coerceAtMost(7) }
    val short:Short = ((byte0.toUInt().shr(n) and 1u)).toShort()
    return (short.toInt())
}