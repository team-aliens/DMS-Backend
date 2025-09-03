package team.aliens.dms.thirdparty.rabbitmq

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("spring.rabbitmq")
class RabbitMqProperties(
    val host: String,
    val port: Int,
    val username: String,
    val password: String,
)
