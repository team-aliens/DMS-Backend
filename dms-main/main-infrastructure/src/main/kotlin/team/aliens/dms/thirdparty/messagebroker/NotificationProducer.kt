package team.aliens.dms.thirdparty.messagebroker

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component

@Component
class NotificationProducer(
    val rabbitTemplate: RabbitTemplate
) {

    fun sendMessage(message: Any) {
        rabbitTemplate.convertAndSend(
            "notification_exchange", "notification",
            message
        )
    }
}
