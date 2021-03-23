package com.oncelabs.blehero.model

import java.nio.charset.Charset
import kotlin.experimental.or

object DataParser {

    fun byteArrayToHexString(byteArray: ByteArray): String{
        var string = ""

        byteArray?.forEach {
            val byteString = String.format("%02X", it)
            string = "$string$byteString"
        }

        return if(string.isBlank()){
            ""
        }else {
            string
        }
    }

    fun byteArrayToAsciiString(byteArray: ByteArray): String{
        var string = ""

        byteArray.forEach {
            string = "$string${it.toChar()}"
        }

        return string
    }

    fun byteArrayToDecimalString(byteArray: ByteArray): String{
        var string = ""

        byteArray.forEach {
            string = "$string${it.toInt()},"
        }

        return "[$string]"
    }

    fun hexStringToByteArray(hexString: String): ByteArray{
        var byteArray = byteArrayOf()
        hexString.forEachIndexed { index, char ->
            if(index%2 != 0){
                byteArray[index/2] = (charToNibble(char).toInt().shl(8).toByte())
            } else{
                byteArray[index/2] = byteArray[index/2].or(charToNibble(char))
            }
        }

        return byteArray
    }

    fun asciiStringToByteArray(asciiString: String): ByteArray{
        return asciiString.toByteArray(Charsets.US_ASCII)
    }

//    fun decimalStringToByteArray(decimalString: String): ByteArray{
//
//
//    }

    fun charToNibble(char: Char): Byte{
        return when(char){
            '0'->{0x0}
            '1'->{0x1}
            '2'->{0x2}
            '3'->{0x3}
            '4'->{0x4}
            '5'->{0x5}
            '6'->{0x6}
            '7'->{0x7}
            '8'->{0x8}
            '9'->{0x9}
            'a','A'->{0xA}
            'b','B'->{0xB}
            'c','C'->{0xC}
            'd','D'->{0xD}
            'e','E'->{0xE}
            'f','F'->{0xF}
            else->{0x0}
        }
    }

    fun convertStringToHex(str: String): String? {
        val chars = str.toCharArray()
        val hex = StringBuffer()
        for (i in chars.indices) {
            hex.append(Integer.toHexString(chars[i].toInt()))
        }
        return hex.toString()
    }

    fun convertHexToString(hex: String): String? {
        val sb = StringBuilder()
        val temp = StringBuilder()

        var i = 0
        while (i < hex.length - 1) {


            //grab the hex in pairs
            val output = hex.substring(i, i + 2)
            //convert hex to decimal
            val decimal = output.toInt(16)
            //convert the decimal to character
            sb.append(decimal.toChar())
            temp.append(decimal)
            i += 2
        }
        println("Decimal : $temp")
        return sb.toString()
    }
}