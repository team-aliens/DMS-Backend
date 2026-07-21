package team.aliens.dms.event.handler

import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import team.aliens.dms.domain.notification.model.Notification
import team.aliens.dms.domain.notification.service.NotificationService
import team.aliens.dms.event.GroupNotificationEvent
import team.aliens.dms.event.NotificationEvent
import team.aliens.dms.event.SingleNotificationEvent
import team.aliens.dms.event.TopicNotificationEvent

@Component
class NotificationEventHandler(
    private val notificationService: NotificationService
) {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleNotification(event: NotificationEvent) {
        when (event) {
            is SingleNotificationEvent -> notificationService.sendMessage(
                notificationService.getDeviceTokenByUserId(event.userId),
                Notification.from(event.notificationInfo)
            )

            is GroupNotificationEvent -> notificationService.sendMessages(
                notificationService.getDiviceTokensByUserIds(event.userIds),
                Notification.from(event.notificationInfo)
            )

            is TopicNotificationEvent -> notificationService.sendMessagesByTopic(
                Notification.from(event.notificationInfo)
            )
        }
    }
}
