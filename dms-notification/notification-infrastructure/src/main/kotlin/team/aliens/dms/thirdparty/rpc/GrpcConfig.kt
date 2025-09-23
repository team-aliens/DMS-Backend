package team.aliens.dms.thirdparty.rpc

import io.grpc.Server
import io.grpc.ServerBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GrpcConfig(
    val grpcProperties: GrpcProperties,
    val notificationServiceGrpcImpl: NotificationServiceGrpcImpl
) {

    @Bean
    fun grpcServer(): Server {
        val server = ServerBuilder
            .forPort(grpcProperties.server.port)
            .addService(notificationServiceGrpcImpl)
            .build()
            .start()

        Runtime.getRuntime().addShutdownHook(
            Thread {
                server.shutdown()
            }
        )
        return server
    }
}
