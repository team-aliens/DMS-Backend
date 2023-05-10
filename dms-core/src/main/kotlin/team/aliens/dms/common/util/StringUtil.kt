package team.aliens.dms.common.util

import java.security.SecureRandom
import java.util.Base64

object StringUtil {

    private const val NUMBER_OF_CHAR_TO_DISPLAY = 3

    fun coveredEmail(email: String): String {
        val index = email.indexOf('@')

        val sb = StringBuilder(email).also {
            for (i in (index / NUMBER_OF_CHAR_TO_DISPLAY) until index) {
                it.setCharAt(i, '*')
            }
        }

        return sb.toString()
    }

    private val RANDOM = SecureRandom()

    fun randomNumber(number: Int): String {
        val codeList: List<Char> = listOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
        val authCodeList: MutableList<String> = mutableListOf()

        for (i: Int in 0 until number) {
            authCodeList.add(i, codeList[RANDOM.nextInt(codeList.size)].toString())
        }

        return authCodeList.toString().replace("[^0-9]".toRegex(), "")
    }

    fun randomKey(byteSize: Int = 24): String =
        Base64.getUrlEncoder().encodeToString(
            ByteArray(byteSize).also { RANDOM.nextBytes(it) }
        )

    fun <T> List<T>.toStringWithoutBracket() = toString().replace("[\\[\\]]".toRegex(), "")
}
