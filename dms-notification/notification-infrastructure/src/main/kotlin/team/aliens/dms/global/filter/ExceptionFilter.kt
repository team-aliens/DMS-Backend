package team.aliens.dms.global.filter

import com.fasterxml.jackson.databind.ObjectMapper
import io.sentry.Sentry
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.web.filter.OncePerRequestFilter
import team.aliens.dms.common.error.DmsException
import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.global.error.ErrorResponse
import team.aliens.dms.global.error.GlobalErrorCode
import team.aliens.dms.thirdparty.slack.SlackAdapter
import java.nio.charset.StandardCharsets

class ExceptionFilter(
    private val objectMapper: ObjectMapper,
    private val slackAdapter: SlackAdapter
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: DmsException) {
            e.printStackTrace()
            errorToJson(e.errorProperty, response)
            Sentry.captureException(e)
        } catch (e: Exception) {
            e.printStackTrace()
            when (e.cause) {
                is DmsException -> {
                    errorToJson((e.cause as DmsException).errorProperty, response)
                    Sentry.captureException(e)
                }
                else -> {
                    errorToJson(GlobalErrorCode.INTERNAL_SERVER_ERROR, response)
                    slackAdapter.sendServerBug(request, response, e)
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
