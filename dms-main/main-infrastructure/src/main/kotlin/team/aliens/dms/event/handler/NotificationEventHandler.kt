package team.aliens.dms.event.handler

import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import team.aliens.dms.event.NotificationOutboxSender
import team.aliens.dms.event.NotificationOutboxSavedEvent

@Component
class NotificationEventHandler(
    private val notificationOutboxSender: NotificationOutboxSender
) {

    // 정상 경로: 커밋 직후 바로 발송한다. 여기서 못 보낸 건(서버 다운·FCM 실패)은 NotificationOutboxScheduler 가 다시 보낸다
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleNotification(event: NotificationOutboxSavedEvent) {
        notificationOutboxSender.send(event.outboxId)
    }
}
