package team.aliens.dms.domain.auth.service

import team.aliens.dms.domain.auth.model.EmailType

interface CheckAuthCodeService {
    fun checkAuthCodeByEmailAndEmailType(email: String, type: EmailType, code: String)

    fun checkAuthCodeByEmail(email: String, code: String)

    fun checkAuthCodeLimitIsVerifiedByEmail(email: String)
}
