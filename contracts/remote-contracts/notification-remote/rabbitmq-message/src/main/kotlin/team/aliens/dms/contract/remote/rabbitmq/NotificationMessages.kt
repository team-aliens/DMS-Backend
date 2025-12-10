package team.aliens.dms.contract.remote.rabbitmq

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import team.aliens.dms.contract.model.notification.NotificationInfo
import java.util.UUID

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = GroupNotificationMessage::class, name = "GroupNotificationMessage"),
    JsonSubTypes.Type(value = SingleNotificationMessage::class, name = "SingleNotificationMessage")
)
sealed class NotificationMessage

data class GroupNotificationMessage(
    val userIds: List<UUID>,
    val notificationInfo: NotificationInfo
) : NotificationMessage() {
    companion object {
        const val TYPE = "GroupNotificationMessage"
    }
}

data class SingleNotificationMessage(
    val userId: UUID,
    val notificationInfo: NotificationInfo
) : NotificationMessage() {
    companion object {
        const val TYPE = "SingleNotificationMessage"
    }
}

data class TopicNotificationMessage(
    val notificationInfo: NotificationInfo
) : NotificationMessage() {
    companion object {
        const val TYPE = "TopicNotificationMessage"
    }
}
