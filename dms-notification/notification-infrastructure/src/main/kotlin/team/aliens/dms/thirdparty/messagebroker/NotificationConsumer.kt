package team.aliens.dms.thirdparty.messagebroker

import org.springframework.amqp.rabbit.annotation.RabbitListener
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
    fun handleNotificationMessage(message: NotificationMessage) {
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
    }
}
