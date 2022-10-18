package team.aliens.dms.global.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.util.*

@ConfigurationProperties(prefix = "secret")
@ConstructorBinding
class SecurityProperties(

    secretKey: String,

    accessExp: Int,

    refreshExp: Int

) {
    val secretKey: String = Base64.getEncoder().encodeToString(secretKey.toByteArray())

    val accessExp: Int = accessExp * 1000

    val refreshExp: Int = refreshExp * 1000
}