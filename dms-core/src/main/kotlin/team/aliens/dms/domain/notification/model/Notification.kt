package team.aliens.dms.domain.notification.model

import team.aliens.dms.domain.notice.model.Notice
import team.aliens.dms.domain.point.model.PointHistory
import java.time.LocalDateTime
import java.util.UUID

sealed class Notification(

    val schoolId: UUID,

    val topic: Topic,

    val linkIdentifier: String?,

    val title: String,

    val content: String,

    val threadId: String,

    val createdAt: LocalDateTime = LocalDateTime.now(),

    private val isSaveRequired: Boolean

) {
    fun toNotificationOfUser(userId: UUID): NotificationOfUser =
        NotificationOfUser(
            userId = userId,
            topic = topic,
            linkIdentifier = linkIdentifier,
            title = title,
            content = content,
            createdAt = createdAt
        )

    fun runIfSaveRequired(function: () -> Unit) {
        if (isSaveRequired) function.invoke()
    }

    class NoticeNotification(
        schoolId: UUID,
        notice: Notice
    ) : Notification(
        schoolId = schoolId,
        topic = Topic.NOTICE,
        linkIdentifier = notice.id.toString(),
        title = notice.title,
        content = "기숙사 공지가 등록되었습니다.",
        threadId = notice.id.toString(),
        isSaveRequired = true
    )

    class PointNotification(
        pointHistory: PointHistory
    ) : Notification(
        schoolId = pointHistory.schoolId,
        topic = Topic.POINT,
        linkIdentifier = pointHistory.id.toString(),
        title = pointHistory.getTitle(),
        content = pointHistory.pointName,
        threadId = pointHistory.id.toString(),
        isSaveRequired = true
    )
}
