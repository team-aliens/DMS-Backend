package team.aliens.dms.thirdparty.rpc

import com.google.protobuf.BoolValue
import com.google.protobuf.Empty
import io.grpc.stub.StreamObserver
import org.springframework.stereotype.Component
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.service.NotificationService
import team.aliens.dms.thirdparty.rpc.mapper.DeviceTokenProtoMapper
import team.aliens.notification.DeviceTokenProto
import team.aliens.notification.NotificationServiceGrpc
import team.aliens.notification.UserIdProto

@Component
class NotificationServiceGrpcImpl(
    private val notificationService: NotificationService,
    private val deviceTokenProtoMapper: DeviceTokenProtoMapper
) : NotificationServiceGrpc.NotificationServiceImplBase() {
    override fun checkDeviceTokenByUserId(
        request: UserIdProto?,
        responseObserver: StreamObserver<BoolValue?>?
    ) {
        val result = notificationService.checkDeviceTokenByUserId(
            request!!.userId.toUUID()
        )

        responseObserver!!.onNext(
            BoolValue.of(result)
        )
        responseObserver.onCompleted()
    }

    override fun deleteDeviceTokenByUserId(
        request: UserIdProto?,
        responseObserver: StreamObserver<Empty?>?
    ) {
        notificationService.deleteDeviceTokenByUserId(request!!.userId.toUUID())
        responseObserver!!.onNext(Empty.getDefaultInstance())
        responseObserver.onCompleted()
    }

    override fun saveDeviceToken(
        request: DeviceTokenProto?,
        responseObserver: StreamObserver<DeviceTokenProto?>?
    ) {
        val deviceTokenInfo = deviceTokenProtoMapper.toInfo(
            request!!
        )
        notificationService.saveDeviceToken(
            DeviceToken.from(deviceTokenInfo)
        )
    }
}
