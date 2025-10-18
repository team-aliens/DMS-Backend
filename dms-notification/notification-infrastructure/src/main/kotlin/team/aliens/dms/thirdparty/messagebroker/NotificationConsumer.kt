package team.aliens.dms.thirdparty.messagebroker

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import team.aliens.dms.GroupNotificationInfo
import team.aliens.dms.SingleNotificationInfo
import team.aliens.dms.TopicNotificationInfo
import team.aliens.dms.domain.notification.model.Notification
import team.aliens.dms.domain.notification.service.NotificationService

@Component
class NotificationConsumer(
    private val notificationService: NotificationService

) {
    @RabbitListener(queues = ["notification_queue"])
    fun handleNotificationMessage(message: TopicNotificationInfo) {

        message.run {
            when (this) {
                is GroupNotificationInfo -> {
                    notificationService.sendMessages(
                        notificationService.getDiviceTokensByUserIds(this.userIds),
                        Notification.from(this.notificationInfo)
                    )
                }

                is SingleNotificationInfo -> {
                    notificationService.sendMessage(
                        notificationService.getDeviceTokenByUserId(this.userId),
                        Notification.from(this.notificationInfo)
                    )
                }

                else -> {
                    notificationService.sendMessagesByTopic(
                        Notification.from(this.notificationInfo)
                    )
                }
            }
        }
    }
}
