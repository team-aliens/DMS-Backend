package team.aliens.dms.common.service

import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.auth.exception.PasswordMismatchException

class SecurityServiceImpl(
    private val securityPort: SecurityPort
) : SecurityService {

    override fun encodePassword(password: String) =
        securityPort.encodePassword(password)

    override fun checkIsPasswordMatches(rawPassword: String, encodedPassword: String) {
        if (!securityPort.isPasswordMatch(rawPassword, encodedPassword)) {
            throw PasswordMismatchException
        }
    }
}