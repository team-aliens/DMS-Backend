package team.aliens.dms.domain.notification.model

import java.time.LocalDateTime
import java.util.UUID

class UserNotification(

    val id: UUID = UUID(0, 0),

    val userId: UUID,

    val topic: Topic,

    val identifier: String?,

    val title: String,

    val content: String,

    val createdAt: LocalDateTime,

    val isRead: Boolean = false

)
