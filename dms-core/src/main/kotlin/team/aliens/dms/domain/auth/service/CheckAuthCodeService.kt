package team.aliens.dms.domain.auth.service

import team.aliens.dms.domain.auth.model.AuthCode
import team.aliens.dms.domain.auth.model.EmailType

interface CheckAuthCodeService {
    fun checkAuthCodeByEmailAndEmailType(email: String, type: EmailType, code: String): AuthCode
}
