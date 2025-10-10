package team.aliens.dms.thirdparty.rpc

import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import team.aliens.dms.contract.remote.notification.grpc.NotificationServiceGrpc

@Configuration
class GrpcConfig(
    private val grpcProperties: GrpcProperties,
) {

    @Bean
    fun notificationManagedChannel(): ManagedChannel {

        return ManagedChannelBuilder.forAddress(
            grpcProperties.client.notificationServer.host,
            grpcProperties.client.notificationServer.port
        ).apply {
            when (grpcProperties.client.notificationServer.negotiationType) {
                "planintext" -> {
                    usePlaintext()
                }
                else -> {
                    usePlaintext()
                }
            }
            if (grpcProperties.client.notificationServer.negotiationType.equals("planintext")) {
                usePlaintext()
            } else {
                usePlaintext()
            }
        }.build()
    }

    @Bean
    fun notificationStub(channel: ManagedChannel): NotificationServiceGrpc.NotificationServiceBlockingStub {
        return NotificationServiceGrpc.newBlockingStub(channel)
    }
}
