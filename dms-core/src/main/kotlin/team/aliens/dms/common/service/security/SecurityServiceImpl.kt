package team.aliens.dms.common.service.security

import java.util.UUID
import team.aliens.dms.common.annotation.Service
import team.aliens.dms.common.model.SchoolSecret
import team.aliens.dms.common.spi.SchoolSecretPort
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.common.util.StringUtil
import team.aliens.dms.domain.auth.exception.PasswordMismatchException

@Service
class SecurityServiceImpl(
    private val securityPort: SecurityPort,
    private val schoolSecretPort: SchoolSecretPort
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

    override fun createSchoolSecretBySchoolId(schoolId: UUID) {
        schoolSecretPort.saveSchoolSecret(
            SchoolSecret(
                schoolId = schoolId,
                secretKey = StringUtil.randomKey()
            )
        )
    }
}
