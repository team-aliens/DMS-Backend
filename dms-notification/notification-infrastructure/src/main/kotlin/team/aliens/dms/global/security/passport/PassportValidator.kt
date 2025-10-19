package team.aliens.dms.global.security.passport

import org.springframework.stereotype.Component
import team.aliens.dms.domain.auth.model.Passport
import java.security.MessageDigest

@Component
class PassportValidator(
    private val passportIntegrityGenerator: PassportIntegrityGenerator
) {
    fun validate(passport: Passport): Boolean {
        val expectedHmac = passportIntegrityGenerator.generate(passport.user)
        return MessageDigest.isEqual(
            expectedHmac.toByteArray(),
            passport.userIntegrity.toByteArray()
        )
    }
}
