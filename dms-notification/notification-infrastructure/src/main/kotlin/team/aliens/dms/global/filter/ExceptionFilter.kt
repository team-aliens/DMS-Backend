package team.aliens.dms.global.filter

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.web.filter.OncePerRequestFilter
import team.aliens.dms.common.error.DmsException
import team.aliens.dms.global.error.ErrorProperty
import team.aliens.dms.global.error.ErrorResponse
import team.aliens.dms.global.error.NotificationErrorCode
import java.nio.charset.StandardCharsets

class ExceptionFilter(
    private val objectMapper: ObjectMapper
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: Exception) {
            when (e) {
                is DmsException -> {
                    setErrorResponse(e.errorProperty, response)
                }
                else -> {
                    e.printStackTrace()
                    setErrorResponse(NotificationErrorCode.INTERNAL_SERVER_ERROR, response)
                }
            }
        }
    }

    private fun setErrorResponse(errorProperty: ErrorProperty, response: HttpServletResponse) {
        response.status = errorProperty.status()
        response.characterEncoding = StandardCharsets.UTF_8.name()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.writer.write(objectMapper.writeValueAsString(ErrorResponse.of(errorProperty)))
    }
}
