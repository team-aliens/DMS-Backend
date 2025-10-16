package team.aliens.dms.global.security

import io.jsonwebtoken.security.Keys
import org.springframework.boot.context.properties.ConfigurationProperties
import java.util.Base64
import javax.crypto.SecretKey

@ConfigurationProperties(prefix = "secret")
class SecurityProperties(
    secretKey: String,
) {
    val secretKey: SecretKey = Keys.hmacShaKeyFor(
        Base64.getEncoder().encodeToString(secretKey.toByteArray())
            .toByteArray(Charsets.UTF_8)
    )
}
