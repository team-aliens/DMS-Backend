package team.aliens.dms.domain.notification.model

import team.aliens.dms.contract.model.notification.NotificationInfo
import team.aliens.dms.contract.model.notification.Topic
import java.time.LocalDateTime
import java.util.UUID

data class Notification(

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

    companion object {
        fun from(notificationInfo: NotificationInfo): Notification {
            return Notification(
                schoolId = notificationInfo.schoolId,
                topic = notificationInfo.topic,
                linkIdentifier = notificationInfo.linkIdentifier,
                title = notificationInfo.title,
                content = notificationInfo.content,
                threadId = notificationInfo.threadId,
                isSaveRequired = notificationInfo.isSaveRequired
            )
        }
    }
}
