package team.aliens.dms.thirdparty.rpc.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("grpc")
data class GrpcProperties(
    val client: Client
) {
    data class Client(
        val notificationServer: NotificationServer
    ) {
        data class NotificationServer(
            val host: String,
            val port: Int,
            val negotiationType: String
        )
    }
}
