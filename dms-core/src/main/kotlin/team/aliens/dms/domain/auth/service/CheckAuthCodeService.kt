package team.aliens.dms.domain.auth.service

interface CheckAuthCodeService {
    fun checkAuthCodeExists(code: String)

    fun checkAuthCodeByEmail(email: String, code: String)

    fun checkAuthCodeLimitIsVerifiedByEmail(email: String)
}
