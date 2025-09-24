package team.aliens.dms.thirdparty.messagebroker.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("rabbitmq")
class RabbitMqProperties(
    val host: String,
    val port: Int,
    val username: String,
    val password: String,
)
