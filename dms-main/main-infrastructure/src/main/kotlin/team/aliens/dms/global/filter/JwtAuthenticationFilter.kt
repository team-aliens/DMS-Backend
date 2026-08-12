package team.aliens.dms.global.filter

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.auth.model.PassportUser
import team.aliens.dms.global.security.exception.InvalidTokenException
import team.aliens.dms.global.security.principle.GeneralTeacherDetails
import team.aliens.dms.global.security.principle.HeadTeacherDetails
import team.aliens.dms.global.security.principle.ManagerDetails
import team.aliens.dms.global.security.principle.StudentDetails
import team.aliens.dms.global.security.token.JwtParser
import team.aliens.dms.global.security.token.JwtProperties

class JwtAuthenticationFilter(
    private val jwtParser: JwtParser
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = resolveTokenOrNull(request)

        if (token != null) {
            val user = jwtParser.extractUserInfo(token)

            SecurityContextHolder.clearContext()
            SecurityContextHolder.getContext().authentication = createAuthentication(user)
        }

        filterChain.doFilter(request, response)
    }

    private fun resolveTokenOrNull(request: HttpServletRequest): String? {
        val authorizationHeader = request.getHeader(JwtProperties.HEADER)
            ?: return null

        if (!authorizationHeader.startsWith(JwtProperties.PREFIX)) {
            throw InvalidTokenException
        }

        return authorizationHeader.removePrefix(JwtProperties.PREFIX)
    }

    private fun createAuthentication(user: PassportUser): UsernamePasswordAuthenticationToken {
        val authorities = listOf(SimpleGrantedAuthority(user.authority.name))

        val details = when (user.authority) {
            Authority.STUDENT -> StudentDetails(
                userId = user.id,
                schoolId = user.schoolId,
                authority = user.authority
            )
            Authority.MANAGER -> ManagerDetails(
                userId = user.id,
                schoolId = user.schoolId,
                authority = user.authority
            )
            Authority.GENERAL_TEACHER -> GeneralTeacherDetails(
                userId = user.id,
                schoolId = user.schoolId,
                authority = user.authority
            )
            Authority.HEAD_TEACHER -> HeadTeacherDetails(
                userId = user.id,
                schoolId = user.schoolId,
                authority = user.authority
            )
        }

        return UsernamePasswordAuthenticationToken(details, null, authorities)
    }
}
