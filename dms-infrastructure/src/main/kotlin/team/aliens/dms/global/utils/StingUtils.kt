package team.aliens.dms.global.utils

import org.springframework.stereotype.Component
import team.aliens.dms.global.util.CoveredEmailPort
import java.lang.StringBuilder

@Component
class StingUtils : CoveredEmailPort {

    override fun coveredEmail(email: String) : String {
        val index = email.indexOf('@')

        val sb = StringBuilder(email).also {
            for (i in (index/3) until index) {
                it.setCharAt(i, '*')
            }
        }

        return sb.toString();
    }
}