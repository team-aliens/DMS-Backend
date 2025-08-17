package team.aliens.dms.global.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import team.aliens.dms.global.security.exception.error.SecurityErrorCode

@Component
class CustomAccessDeniedHandler(
    private val objectMapper: ObjectMapper
) : AccessDeniedHandler {

    override fun handle(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        accessDeniedException: AccessDeniedException?
    ) {

        val errorCode = SecurityErrorCode.INVALID_ROLE

        response?.apply {
            contentType = "application/json"
            characterEncoding = "UTF-8"
            status = errorCode.status()

            val errorResponse = mapOf(
                "status" to errorCode.status(),
                "message" to errorCode.message(),
                "code" to errorCode.code()
            )

            writer.write(objectMapper.writeValueAsString(errorResponse))
        }
    }
}
