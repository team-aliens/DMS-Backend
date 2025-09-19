package team.aliens.dms.thirdparty.rpc.notification.mapper

import org.springframework.stereotype.Component
import team.aliens.dms.contract.model.DeviceTokenInfo
import team.aliens.dms.thirdparty.rpc.notification.converter.toByteString
import team.aliens.dms.thirdparty.rpc.notification.converter.toUUID
import team.aliens.notification.DeviceTokenProto

@Component
class DeviceTokenProtoMapper {

    fun toProto(deviceTokenInfo: DeviceTokenInfo): DeviceTokenProto {
        return DeviceTokenProto.newBuilder()
            .setId(deviceTokenInfo.id.toByteString())
            .setToken(deviceTokenInfo.token)
            .setSchoolId(deviceTokenInfo.schoolId.toByteString())
            .setUserId(deviceTokenInfo.userId.toByteString())
            .build()
    }

    fun toInfo(deviceTokenProto: DeviceTokenProto): DeviceTokenInfo {
        return DeviceTokenInfo(
            id = deviceTokenProto.id.toUUID(),
            token = deviceTokenProto.token,
            schoolId = deviceTokenProto.schoolId.toUUID(),
            userId = deviceTokenProto.userId.toUUID()
        )
    }
}
