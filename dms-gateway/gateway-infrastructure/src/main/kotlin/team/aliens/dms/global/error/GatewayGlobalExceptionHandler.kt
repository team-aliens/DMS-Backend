package team.aliens.dms.global.error

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.core.annotation.Order
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import team.aliens.dms.common.error.DmsException
import java.nio.charset.StandardCharsets

@Component
@Order(-2)
class GatewayGlobalExceptionHandler(
    private val objectMapper: ObjectMapper
) : ErrorWebExceptionHandler {

    override fun handle(
        exchange: ServerWebExchange,
        ex: Throwable
    ): Mono<Void> {

        val errorCode = when (ex) {
            is DmsException -> ex.errorProperty
            is ResponseStatusException -> {
                when (ex.statusCode.value()) {
                    400 -> GlobalErrorCode.BAD_REQUEST
                    else -> GlobalErrorCode.INTERNAL_SERVER_ERROR
                }
            }
            else -> GlobalErrorCode.INTERNAL_SERVER_ERROR
        }

        exchange.response.apply {
            this.setRawStatusCode(errorCode.status())
            this.headers.contentType = MediaType("application", "json", StandardCharsets.UTF_8)

            val errorResponse = mapOf(
                "status" to errorCode.status(),
                "message" to errorCode.message(),
                "code" to errorCode.code()
            )

            val responseBody = objectMapper.writeValueAsString(errorResponse)
            val buffer: DataBuffer = bufferFactory().wrap(responseBody.toByteArray())

            return writeWith(Mono.just(buffer))
        }
    }
}
