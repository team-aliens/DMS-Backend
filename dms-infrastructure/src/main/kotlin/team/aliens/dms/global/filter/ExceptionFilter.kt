package team.aliens.dms.global.filter

import com.fasterxml.jackson.databind.ObjectMapper
import io.sentry.Sentry
import org.springframework.http.MediaType
import org.springframework.web.filter.OncePerRequestFilter
import team.aliens.dms.common.error.DmsException
import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.global.error.ErrorResponse
import team.aliens.dms.global.exception.InternalServerErrorException
import java.nio.charset.StandardCharsets
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

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
        } catch (e: DmsException) {
            errorToJson(e.errorProperty, response)
            Sentry.captureException(e)
        } catch (e: Exception) {
            when (e.cause) {
                is DmsException -> {
                    errorToJson((e.cause as DmsException).errorProperty, response)
                }
                else -> {
                    errorToJson(InternalServerErrorException.errorProperty, response)
                    Sentry.captureException(e)
                }
            }
        }
    }

    private fun errorToJson(errorProperty: ErrorProperty, response: HttpServletResponse) {
        response.status = errorProperty.status()
        response.characterEncoding = StandardCharsets.UTF_8.name()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.writer.write(objectMapper.writeValueAsString(ErrorResponse.of(errorProperty)))
    }
}
