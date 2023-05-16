package team.aliens.dms.common.service.security

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.auth.exception.PasswordMismatchException

@Service
class SecurityServiceImpl(
    private val securityPort: SecurityPort
) : SecurityService {

    override fun encodePassword(password: String) =
        securityPort.encodePassword(password)

    override fun getCurrentUserId() =
        securityPort.getCurrentUserId()

    override fun checkIsPasswordMatches(rawPassword: String, encodedPassword: String) {
        if (!securityPort.isPasswordMatch(rawPassword, encodedPassword)) {
            throw PasswordMismatchException
        }
    }
}
