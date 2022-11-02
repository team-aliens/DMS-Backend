package team.aliens.dms.global.util

import org.springframework.stereotype.Component
import team.aliens.dms.common.spi.CoveredEmailPort
import team.aliens.dms.common.spi.ReceiveRandomStringPort
import java.security.SecureRandom

@Component
class StingUtil : CoveredEmailPort, ReceiveRandomStringPort {

    override fun coveredEmail(email: String): String {
        val index = email.indexOf('@')

        val sb = StringBuilder(email).also {
            for (i in (index / 3) until index) {
                it.setCharAt(i, '*')
            }
        }

        return sb.toString();
    }

    override fun randomNumber(number: Int): String {
        val random = SecureRandom()
        val codeList: List<Char> = listOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9')
        val authCodeList: MutableList<String> = mutableListOf()

        for (i: Int in 0..number) {
            authCodeList.add(i, codeList[random.nextInt(10)].toString());
        }

        return authCodeList.toString().replace("[^0-9]".toRegex(), "")
    }

}