package team.aliens.dms

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import team.aliens.dms.contract.model.notification.NotificationInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = GroupNotificationInfo::class, name = "GroupNotificationInfo"),
    JsonSubTypes.Type(value = SingleNotificationInfo::class, name = "SingleNotificationInfo")
)
open class TopicNotificationInfo(
    open val notificationInfo: NotificationInfo
)
