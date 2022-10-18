package team.aliens.dms.global.filter

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import org.springframework.web.filter.OncePerRequestFilter
import team.aliens.dms.global.error.DmsException
import team.aliens.dms.global.error.ErrorProperty
import team.aliens.dms.global.error.ErrorResponse
import team.aliens.dms.global.exception.InternalServerErrorException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 *
 * 발생하는 예외를 처리하기 위한 ExceptionFilter
 *
 * @author leejeongyoon
 * @date 2022/09/22
 * @version 1.0.0
 **/
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
            when(e) {
                is DmsException -> errorToJson((e.cause as DmsException).errorProperty, response)
                else -> {
                    errorToJson(InternalServerErrorException.errorProperty, response)
                    e.printStackTrace()
                }
            }
        }
    }

    private fun errorToJson(errorProperty: ErrorProperty, response: HttpServletResponse) {
        response.status = errorProperty.status()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.writer.write(objectMapper.writeValueAsString(ErrorResponse.of(errorProperty)))
    }
}