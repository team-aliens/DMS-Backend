package team.aliens.dms.thirdparty.rpc

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("grpc")
data class GrpcProperties(
    val server: Server
) {
    data class Server(
        val port: Int,
        val allowedIps: Set<String>
    )
}
