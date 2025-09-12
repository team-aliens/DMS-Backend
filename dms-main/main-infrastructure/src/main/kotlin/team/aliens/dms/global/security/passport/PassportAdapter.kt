package team.aliens.dms.global.security.passport

import org.springframework.stereotype.Component
import team.aliens.dms.domain.auth.model.Passport
import team.aliens.dms.domain.auth.model.PassportUser
import team.aliens.dms.domain.auth.spi.QueryPassportPort
import team.aliens.dms.global.security.exception.InvalidTokenException
import team.aliens.dms.global.security.principle.ManagerDetails
import team.aliens.dms.global.security.principle.StudentDetails
import team.aliens.dms.global.security.token.JwtParser

@Component
class PassportAdapter(
    private val jwtParser: JwtParser,
    private val passportIntegrityGenerator: PassportIntegrityGenerator
) : QueryPassportPort {

    override fun generatePassportByToken(token: String): Passport {
        val authentication = jwtParser.getAuthentication(token)
        val principal = authentication.principal

        val passportUser = when (principal) {
            is StudentDetails -> PassportUser(
                id = principal.userId,
                schoolId = principal.schoolId,
                authority = principal.authority
            )
            is ManagerDetails -> PassportUser(
                id = principal.userId,
                schoolId = principal.schoolId,
                authority = principal.authority
            )
            else -> throw InvalidTokenException
        }

        val userIntegrity = passportIntegrityGenerator.generate(passportUser)

        return Passport(
            user = passportUser,
            userIntegrity = userIntegrity
        )
    }
}
