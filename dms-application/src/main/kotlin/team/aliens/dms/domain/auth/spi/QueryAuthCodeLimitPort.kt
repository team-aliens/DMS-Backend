package team.aliens.dms.domain.auth.spi

import team.aliens.dms.domain.auth.model.AuthCodeLimit
import team.aliens.dms.domain.auth.model.EmailType
import java.util.UUID

interface QueryAuthCodeLimitPort {
    fun queryAuthCodeLimitByEmailAndEmailType(email: String, type: EmailType) : AuthCodeLimit?
}