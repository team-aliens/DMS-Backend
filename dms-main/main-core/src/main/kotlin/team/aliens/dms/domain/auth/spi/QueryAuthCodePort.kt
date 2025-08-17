package team.aliens.dms.domain.auth.spi

import team.aliens.dms.domain.auth.model.AuthCode
import team.aliens.dms.domain.auth.model.EmailType

interface QueryAuthCodePort {

    fun queryAuthCodeByEmailAndEmailType(email: String, type: EmailType): AuthCode?

    fun existsAuthCodeByCode(code: String): Boolean

    fun queryAuthCodeByEmail(email: String): AuthCode?
}
