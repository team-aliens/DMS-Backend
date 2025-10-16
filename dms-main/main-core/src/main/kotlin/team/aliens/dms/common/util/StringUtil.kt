package team.aliens.dms.common.util

import java.security.SecureRandom

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

    fun randomNumber(number: Int): String {
        val random = SecureRandom()
        val codeList: List<Char> = listOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
        val authCodeList: MutableList<String> = mutableListOf()

        for (i: Int in 0 until number) {
            authCodeList.add(i, codeList[random.nextInt(codeList.size)].toString())
        }

        return authCodeList.toString().replace("[^0-9]".toRegex(), "")
    }

    fun <T> List<T>.toStringWithoutBracket() = toString().replace("[\\[\\]]".toRegex(), "")
}
