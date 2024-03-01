package team.aliens.dms.event

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import team.aliens.dms.domain.notification.service.NotificationService

@Component
class NotificationEventHandler(
    private val notificationService: NotificationService
) {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onNotificationEvent(event: TopicNotificationEvent) {
        event.run {
            when (this) {
                is SingleNotificationEvent -> {
                    notificationService.sendMessage(
                        deviceToken = deviceToken,
                        notification = notification
                    )
                }
                is GroupNotificationEvent -> {
                    notificationService.sendMessages(
                        deviceTokens = deviceTokens,
                        notification = notification
                    )
                }
                else -> {
                    notificationService.sendMessagesByTopic(
                        notification = event.notification
                    )
                }
            }
        }
    }
}
