package team.aliens.dms.stub

import team.aliens.dms.contract.model.notification.Topic
import team.aliens.dms.domain.notification.model.NotificationOfUser
import java.time.LocalDateTime
import java.util.UUID

internal fun createNotificationOfUserStub(
    id: UUID = UUID.randomUUID(),
    userId: UUID = UUID.randomUUID(),
    topic: Topic = Topic.NOTICE,
    linkIdentifier: String? = null,
    title: String = "Test Notification Title",
    content: String = "Test notification content",
    createdAt: LocalDateTime = LocalDateTime.now(),
    isRead: Boolean = false
) = NotificationOfUser(
    id = id,
    userId = userId,
    topic = topic,
    linkIdentifier = linkIdentifier,
    title = title,
    content = content,
    createdAt = createdAt,
    isRead = isRead
)
