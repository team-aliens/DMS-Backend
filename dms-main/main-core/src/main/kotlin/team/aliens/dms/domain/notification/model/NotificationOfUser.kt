package team.aliens.dms.domain.notification.model

import java.time.LocalDateTime
import java.util.UUID

data class NotificationOfUser(

    val id: UUID = UUID(0, 0),

    val userId: UUID,

    val topic: Topic,

    val linkIdentifier: String?,

    val title: String,

    val content: String,

    val createdAt: LocalDateTime,

    val isRead: Boolean = false

) {
    fun read() = this.copy(isRead = true)
}
