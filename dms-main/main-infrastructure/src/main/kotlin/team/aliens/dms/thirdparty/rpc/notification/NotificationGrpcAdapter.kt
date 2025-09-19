package team.aliens.dms.thirdparty.rpc.notification

import org.springframework.stereotype.Component
import team.aliens.dms.contract.model.DeviceTokenInfo
import team.aliens.dms.external.notification.spi.NotificationPort
import team.aliens.dms.thirdparty.rpc.notification.converter.toByteString
import team.aliens.dms.thirdparty.rpc.notification.mapper.DeviceTokenProtoMapper
import java.util.UUID

@Component
class NotificationGrpcAdapter(
    private val notificationStub: NotificationServiceGrpc.NotificationServiceBlockingStub,
    private val deviceTokenProtoMapper: DeviceTokenProtoMapper
) : NotificationPort {
    override fun saveDeviceToken(deviceTokenInfo: DeviceTokenInfo): DeviceTokenInfo {

        return deviceTokenProtoMapper.toInfo(
            notificationStub.saveDeviceToken(
                deviceTokenProtoMapper.toProto(deviceTokenInfo)
            )
        )
    }

    override fun deleteDeviceTokenByUserId(userId: UUID) {
        notificationStub.deleteDeviceTokenByUserId(
            UserIdProto.newBuilder()
                .setUserId(userId.toByteString())
                .build()
        )
    }

    override fun checkDeviceTokenByUserId(userId: UUID): Boolean {
        return notificationStub.checkDeviceTokenByUserId(
            UserIdProto.newBuilder()
                .setUserId(userId.toByteString())
                .build()
        ).value
    }
}
