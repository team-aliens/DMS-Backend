package team.aliens.dms.event

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import team.aliens.dms.domain.notification.model.NotificationOutbox
import team.aliens.dms.domain.notification.service.NotificationService
import java.util.UUID

@Component
class NotificationOutboxSender(
    private val notificationService: NotificationService
) {

    private val log = LoggerFactory.getLogger(this::class.java)

    fun send(outboxId: UUID) {
        val remaining = try {
            notificationService.processNotificationOutbox(outboxId)
        } catch (e: Exception) {
            // 발송 트랜잭션은 롤백됐다. 재시도 횟수를 올리지 않으면 스케줄러가 매번 같은 행을 다시 집는다
            log.warn("Failed to send notification outbox: {}", outboxId, e)
            notificationService.deferNotificationOutbox(outboxId)
        }
        remaining?.let { logIfFailed(it) }
    }

    fun resend() {
        notificationService.getPendingNotificationOutboxIds(BATCH_SIZE).forEach { send(it) }
    }

    private fun logIfFailed(outbox: NotificationOutbox) {
        if (outbox.isFailed()) {
            log.error(
                "[OUTBOX_FAILED] Notification outbox exhausted retries: id={}, topic={}, remainingUsers={}",
                outbox.id, outbox.notificationInfo.topic, outbox.userIds.size
            )
        }
    }

    companion object {
        private const val BATCH_SIZE = 100
    }
}
