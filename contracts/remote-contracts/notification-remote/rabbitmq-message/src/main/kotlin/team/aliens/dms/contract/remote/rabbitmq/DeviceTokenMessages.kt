package team.aliens.dms.contract.remote.rabbitmq

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import team.aliens.dms.contract.model.notification.DeviceTokenInfo
import java.util.UUID

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = SaveDeviceTokenMessage::class, name = "SaveDeviceTokenMessage"),
    JsonSubTypes.Type(value = DeleteDeviceTokenMessage::class, name = "DeleteDeviceTokenMessage")
)
sealed class DeviceTokenMessage

data class SaveDeviceTokenMessage(
    val deviceTokenInfo: DeviceTokenInfo
) : DeviceTokenMessage() {
    companion object {
        const val TYPE = "SaveDeviceTokenMessage"
    }
}

data class DeleteDeviceTokenMessage(
    val userId: UUID
) : DeviceTokenMessage() {
    companion object {
        const val TYPE = "DeleteDeviceTokenMessage"
    }
}
