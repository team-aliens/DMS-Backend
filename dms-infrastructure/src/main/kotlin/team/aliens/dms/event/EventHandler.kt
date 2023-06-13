package team.aliens.dms.event

import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import team.aliens.dms.domain.notification.spi.NotificationPort

@Component
class NotificationEventHandler(
    private val notificationPort: NotificationPort
) {

    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onNotificationEvent(event: GroupNotificationEvent) {

        if (event is SingleNotificationEvent) {
            notificationPort.sendMessage(
                deviceToken = event.deviceToken,
                notification = event.notification
            )
        } else {
            notificationPort.sendToAll(
                notification = event.notification
            )
        }
    }
}
