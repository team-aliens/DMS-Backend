package team.aliens.dms.thirdparty.messagebroker

import com.rabbitmq.client.Channel
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.amqp.support.AmqpHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.stereotype.Component
import team.aliens.dms.contract.remote.rabbitmq.GroupNotificationMessage
import team.aliens.dms.contract.remote.rabbitmq.NotificationMessage
import team.aliens.dms.contract.remote.rabbitmq.SingleNotificationMessage
import team.aliens.dms.contract.remote.rabbitmq.TopicNotificationMessage
import team.aliens.dms.domain.notification.model.Notification
import team.aliens.dms.domain.notification.service.NotificationService

@Component
class NotificationConsumer(
    private val notificationService: NotificationService
) {

    @RabbitListener(queues = ["notification_queue"])
    fun handleNotificationMessage(
        message: NotificationMessage,
        channel: Channel,
        @Header(AmqpHeaders.DELIVERY_TAG) deliveryTag: Long
    ) {
        try {
            when (message) {
                is GroupNotificationMessage -> {
                    notificationService.sendMessages(
                        notificationService.getDiviceTokensByUserIds(message.userIds),
                        Notification.from(message.notificationInfo)
                    )
                }

                is SingleNotificationMessage -> {
                    notificationService.sendMessage(
                        notificationService.getDeviceTokenByUserId(message.userId),
                        Notification.from(message.notificationInfo)
                    )
                }

                is TopicNotificationMessage -> {
                    notificationService.sendMessagesByTopic(
                        Notification.from(message.notificationInfo)
                    )
                }
            }

            channel.basicAck(deliveryTag, false)

        } catch (e: Exception) {
            channel.basicNack(deliveryTag, false, true)
            throw e
        }
    }
}