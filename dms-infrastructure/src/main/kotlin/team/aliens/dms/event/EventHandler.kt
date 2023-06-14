package team.aliens.dms.event

import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import team.aliens.dms.domain.notification.spi.NotificationPort

@Component
class NotificationEventHandler(
    private val notificationPort: NotificationPort
) {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onNotificationEvent(event: TopicNotificationEvent) {

        when (event) {
            is SingleNotificationEvent -> {
                notificationPort.sendMessage(
                    deviceToken = event.deviceToken,
                    notification = event.notification
                )
            }
            is GroupNotificationEvent -> {
                notificationPort.sendMessages(
                    deviceTokens = event.deviceTokens,
                    notification = event.notification
                )
            }
            else -> {
                notificationPort.sendByTopic(
                    notification = event.notification
                )
            }
        }
    }
}
