package team.aliens.dms.domain.notification.model

import team.aliens.dms.contract.model.notification.NotificationInfo
import java.util.UUID

// 알림 발송 요청을 비즈니스 트랜잭션과 같이 커밋해 두는 행.
// 커밋 직후 비동기로 바로 발송하고, 서버가 죽거나 FCM 이 실패해 남은 행은 스케줄러가 다시 실행한다.
data class NotificationOutbox(

    val id: UUID = UUID(0, 0),

    val userIds: List<UUID>,

    val notificationInfo: NotificationInfo,

    val status: NotificationOutboxStatus = NotificationOutboxStatus.PENDING,

    val retryCount: Int = 0

) {

    // 발송 시도 전체가 롤백된 경우: 대상, 알림함 저장 여부를 그대로 두고 다시 시도한다
    fun retryAll(): NotificationOutbox = nextAttempt()

    // 일부 토큰만 일시적으로 실패한 경우: 알림함은 이미 저장됐으므로 실패한 유저에게 푸시만 다시 보낸다
    fun retryFor(failedUserIds: List<UUID>): NotificationOutbox =
        copy(
            userIds = failedUserIds,
            notificationInfo = notificationInfo.withoutSave()
        ).nextAttempt()

    fun isFailed() = status == NotificationOutboxStatus.FAILED

    private fun nextAttempt(): NotificationOutbox {
        val nextRetryCount = retryCount + 1
        if (nextRetryCount > MAX_RETRY_COUNT) {
            return copy(status = NotificationOutboxStatus.FAILED, retryCount = nextRetryCount)
        }
        return copy(retryCount = nextRetryCount)
    }

    private fun NotificationInfo.withoutSave() = NotificationInfo(
        schoolId = schoolId,
        topic = topic,
        pointDetailTopic = pointDetailTopic,
        linkIdentifier = linkIdentifier,
        title = title,
        content = content,
        threadId = threadId,
        createdAt = createdAt,
        isSaveRequired = false
    )

    companion object {
        const val MAX_RETRY_COUNT = 5
    }
}

enum class NotificationOutboxStatus {
    PENDING,
    FAILED
}
