package team.aliens.dms.global.util

import org.springframework.stereotype.Component
import team.aliens.dms.global.spi.CoveredEmailPort
import java.lang.StringBuilder

@Component
class StingUtil : CoveredEmailPort {

    override fun coveredEmail(email: String): String {
        val index = email.indexOf('@')

        val sb = StringBuilder(email).also {
            for (i in (index / 3) until index) {
                it.setCharAt(i, '*')
            }
        }

        return sb.toString();
    }
}