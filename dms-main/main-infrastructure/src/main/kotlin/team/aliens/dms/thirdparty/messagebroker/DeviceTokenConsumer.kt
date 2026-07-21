package team.aliens.dms.thirdparty.messagebroker

import com.rabbitmq.client.Channel
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.support.AmqpHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Component
import team.aliens.dms.contract.remote.rabbitmq.DeleteDeviceTokenMessage
import team.aliens.dms.contract.remote.rabbitmq.DeviceTokenMessage
import team.aliens.dms.contract.remote.rabbitmq.SaveDeviceTokenMessage
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.service.NotificationService

@Component
class DeviceTokenConsumer(
    private val notificationService: NotificationService
) {

    @RabbitListener(queues = ["device_token_queue"])
    fun handleDeviceTokenMessage(
        message: DeviceTokenMessage,
        channel: Channel,
        @Header(AmqpHeaders.DELIVERY_TAG) deliveryTag: Long
    ) {
        try {
            when (message) {
                is SaveDeviceTokenMessage -> {
                    notificationService.saveDeviceToken(
                        DeviceToken.from(message.deviceTokenInfo)
                    )
                }

                is DeleteDeviceTokenMessage -> {
                    notificationService.deleteDeviceTokenByUserId(message.userId)
                }
            }

            channel.basicAck(deliveryTag, false)

        } catch (e: Exception) {
            channel.basicNack(deliveryTag, false, true)
            throw e
        }
    }
}