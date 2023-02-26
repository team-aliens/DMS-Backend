package team.aliens.dms.global.security

import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component
import team.aliens.dms.global.security.exception.InvalidRoleException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class CustomAccessDeniedHandler : AccessDeniedHandler {

    override fun handle(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        accessDeniedException: AccessDeniedException?
    ) {
        throw InvalidRoleException
    }
}
