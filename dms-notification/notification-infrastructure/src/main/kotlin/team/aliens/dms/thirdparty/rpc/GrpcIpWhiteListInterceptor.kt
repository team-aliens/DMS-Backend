package team.aliens.dms.thirdparty.rpc

import io.grpc.Grpc
import io.grpc.Metadata
import io.grpc.ServerCall
import io.grpc.ServerCallHandler
import io.grpc.ServerInterceptor
import io.grpc.Status
import java.net.InetSocketAddress

class GrpcIpWhiteListInterceptor(val allowedIps: Set<String>) : ServerInterceptor {
    override fun <ReqT : Any?, RespT : Any?> interceptCall(
        call: ServerCall<ReqT?, RespT?>?,
        headers: Metadata?,
        next: ServerCallHandler<ReqT?, RespT?>?
    ): ServerCall.Listener<ReqT?>? {
        val remoteIp = (call!!.attributes[Grpc.TRANSPORT_ATTR_REMOTE_ADDR] as? InetSocketAddress)
            ?.address
            ?.hostAddress

        if (remoteIp == null || remoteIp !in allowedIps) {
            call.close(
                Status.PERMISSION_DENIED.withDescription("IP not allowed: $remoteIp"),
                headers
            )

            return object : ServerCall.Listener<ReqT?>() {}
        }

        return next!!.startCall(call, headers)
    }
}
