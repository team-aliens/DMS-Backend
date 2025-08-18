package team.aliens.dms.global.security

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import team.aliens.dms.global.security.exception.error.SecurityErrorCode

@Component
class CustomAuthenticationEntryPoint(
    private val objectMapper: ObjectMapper
) : AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authException: AuthenticationException?
    ) {

        val errorCode = SecurityErrorCode.FORBIDDEN

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
