package team.aliens.dms.global.filter

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.web.filter.OncePerRequestFilter
import team.aliens.dms.common.error.DmsException
import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.global.error.ErrorResponse
import team.aliens.dms.global.error.GlobalErrorCode
import java.nio.charset.StandardCharsets

/**
 * 필터 체인(JwtAuthenticationFilter 등)에서 던진 예외를 JSON 응답으로 바꾼다.
 * 컨트롤러/유스케이스/서비스에서 던진 예외는 DispatcherServlet 안에서 GlobalErrorHandler가 처리하므로 여기까지 오지 않는다.
 **/
class ExceptionFilter(
    private val objectMapper: ObjectMapper,
) : OncePerRequestFilter() {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: DmsException) {
            logBusinessException(e)
            errorToJson(e.errorProperty, response)
        } catch (e: Exception) {
            log.error("unexpected exception", e)
            errorToJson(GlobalErrorCode.INTERNAL_SERVER_ERROR, response)
        }
    }

    private fun logBusinessException(e: DmsException) {
        log.info("business exception: {} {}", e.errorProperty.code(), e.errorProperty.message())
    }

    private fun errorToJson(errorProperty: ErrorProperty, response: HttpServletResponse) {
        response.status = errorProperty.status()
        response.characterEncoding = StandardCharsets.UTF_8.name()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.writer.write(objectMapper.writeValueAsString(ErrorResponse.of(errorProperty)))
    }
}
