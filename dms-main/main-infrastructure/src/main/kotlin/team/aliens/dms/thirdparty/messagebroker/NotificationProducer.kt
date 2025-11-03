package team.aliens.dms.thirdparty.messagebroker

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component
import team.aliens.dms.TopicDeviceTokenMessage

@Component
class NotificationProducer(
    val rabbitTemplate: RabbitTemplate
) {

    fun sendMessage(message: Any) {
        val routingKey = when (message) {
            is TopicDeviceTokenMessage -> "device_token"
            else -> "notification"
        }

        rabbitTemplate.convertAndSend(
            "notification_exchange", routingKey,
            message
        )
    }
}
