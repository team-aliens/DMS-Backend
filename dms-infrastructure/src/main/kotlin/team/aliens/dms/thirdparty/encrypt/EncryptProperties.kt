package team.aliens.dms.thirdparty.encrypt

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("encrypt")
@ConstructorBinding
class EncryptProperties(
    val transformation: String,
    val algorithm: String
)