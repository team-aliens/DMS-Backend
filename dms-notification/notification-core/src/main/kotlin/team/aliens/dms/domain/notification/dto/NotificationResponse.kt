package team.aliens.dms.domain.notification.dto

import team.aliens.dms.contract.model.notification.PointDetailTopic
import team.aliens.dms.contract.model.notification.Topic
import team.aliens.dms.domain.notification.model.NotificationOfUser
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
    val pointDetailTopic: PointDetailTopic?,
    val linkIdentifier: String?,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime,
    val isRead: Boolean
) {
    companion object {
        fun of(notificationOfUser: NotificationOfUser) = notificationOfUser.run {
            NotificationResponse(
                id = id,
                topic = topic,
                pointDetailTopic = pointDetailTopic,
                linkIdentifier = linkIdentifier,
                title = title,
                content = content,
                createdAt = createdAt,
                isRead = isRead
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
