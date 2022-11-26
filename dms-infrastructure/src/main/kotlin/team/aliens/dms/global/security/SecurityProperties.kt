package team.aliens.dms.global.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.util.Base64

@ConfigurationProperties(prefix = "secret")
@ConstructorBinding
class SecurityProperties(
    secretKey: String,
    val accessExp: Int,
    val refreshExp: Int
) {
    val secretKey: String = Base64.getEncoder().encodeToString(secretKey.toByteArray())
}