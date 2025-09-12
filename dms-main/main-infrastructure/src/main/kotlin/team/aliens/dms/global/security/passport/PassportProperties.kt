package team.aliens.dms.global.security.passport

import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.ConfigurationProperties
import javax.crypto.SecretKey

@ConfigurationProperties(prefix = "passport")
class PassportProperties(
    passportSecretKey: String
) {
    val secretKey: SecretKey = Keys.hmacShaKeyFor(
        passportSecretKey.toByteArray(Charsets.UTF_8)
    )
}
