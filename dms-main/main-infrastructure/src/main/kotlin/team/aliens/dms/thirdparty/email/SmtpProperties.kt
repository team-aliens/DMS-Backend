package team.aliens.dms.thirdparty.email

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("spring.mail")
class SmtpProperties(
    val host: String,
    val port: Int,
    val username: String,
    val password: String,
    val properties: Map<String, String> = emptyMap()
)
