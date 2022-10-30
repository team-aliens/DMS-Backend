package team.aliens.dms.domain.auth.spi

import team.aliens.dms.domain.auth.model.AuthCodeLimit
import team.aliens.dms.domain.auth.model.EmailType

interface QueryAuthCodeLimitPort {

    fun queryAuthCodeLimitByEmailAndEmailType(email: String, type: EmailType): AuthCodeLimit?

}