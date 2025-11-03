package team.aliens.dms

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = SaveDeviceTokenMessage::class, name = "SaveDeviceTokenMessage"),
    JsonSubTypes.Type(value = DeleteDeviceTokenMessage::class, name = "DeleteDeviceTokenMessage")
)
open class TopicDeviceTokenMessage
