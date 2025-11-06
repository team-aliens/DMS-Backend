package team.aliens.dms.stub

import team.aliens.dms.contract.model.notification.Topic
import team.aliens.dms.domain.notification.model.Notification
import java.time.LocalDateTime
import java.util.UUID

internal fun createNotificationStub(
    schoolId: UUID = UUID.randomUUID(),
    topic: Topic = Topic.NOTICE,
    linkIdentifier: String? = null,
    title: String = "Test Notification Title",
    content: String = "Test notification content",
    threadId: String = UUID.randomUUID().toString(),
    createdAt: LocalDateTime = LocalDateTime.now(),
    isSaveRequired: Boolean = true
) = Notification(
    schoolId = schoolId,
    topic = topic,
    linkIdentifier = linkIdentifier,
    title = title,
    content = content,
    threadId = threadId,
    createdAt = createdAt,
    isSaveRequired = isSaveRequired
)
