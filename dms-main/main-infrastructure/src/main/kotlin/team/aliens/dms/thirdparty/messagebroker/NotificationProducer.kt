package team.aliens.dms.thirdparty.messagebroker

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component
import team.aliens.dms.contract.remote.rabbitmq.DeviceTokenMessage
import team.aliens.dms.contract.remote.rabbitmq.NotificationMessage
import team.aliens.dms.thirdparty.messagebroker.exception.UnknownMessageException

@Component
class NotificationProducer(
    val rabbitTemplate: RabbitTemplate
) {

    fun sendMessage(message: Any) {
        val routingKey = when (message) {
            is DeviceTokenMessage -> "device_token"
            is NotificationMessage -> "notification"
            else -> {
                throw UnknownMessageException
            }
        }

        rabbitTemplate.convertAndSend(
            "notification_exchange", routingKey,
            message
        )
    }
}
