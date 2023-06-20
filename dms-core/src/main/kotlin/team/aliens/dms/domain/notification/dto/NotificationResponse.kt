package team.aliens.dms.domain.notification.dto

import team.aliens.dms.domain.notification.model.NotificationOfUser
import team.aliens.dms.domain.notification.model.Topic
import java.time.LocalDateTime
import java.util.UUID

data class NotificationsResponse(
    val notifications: List<NotificationResponse>
) {
    companion object {
        fun of(notificationOfUsers: List<NotificationOfUser>) =
            NotificationsResponse(
                notificationOfUsers.map { NotificationResponse.of(it) }
            )
    }
}

data class NotificationResponse(
    val id: UUID,
    val topic: Topic,
    val identifier: String?,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime
) {
    companion object {
        fun of(notificationOfUser: NotificationOfUser) = notificationOfUser.run {
            NotificationResponse(
                id = id,
                topic = topic,
                identifier = identifier,
                title = title,
                content = content,
                createdAt = createdAt
            )
        }
    }
}

data class TopicSubscribesResponse(
    val topicSubscribes: List<TopicSubscribeResponse>
)

data class TopicSubscribeResponse(
    val topic: Topic,
    val isSubscribed: Boolean
)
