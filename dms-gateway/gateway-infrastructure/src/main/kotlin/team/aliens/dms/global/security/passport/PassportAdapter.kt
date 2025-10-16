package team.aliens.dms.global.security.passport

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import team.aliens.dms.domain.auth.model.Passport
import team.aliens.dms.domain.auth.model.PassportUser
import team.aliens.dms.global.security.token.JwtParser

@Component
class PassportAdapter(
    private val jwtParser: JwtParser,
    private val passportIntegrityGenerator: PassportIntegrityGenerator
) {

    fun generatePassportByToken(token: String): Mono<Passport> {
        return jwtParser.extractUserInfo(token)
            .map { userInfo ->
                val passportUser = PassportUser(
                    id = userInfo.userId,
                    schoolId = userInfo.schoolId,
                    authority = userInfo.authority
                )

                val userIntegrity = passportIntegrityGenerator.generate(passportUser)

                Passport(
                    user = passportUser,
                    userIntegrity = userIntegrity
                )
            }
    }
}
