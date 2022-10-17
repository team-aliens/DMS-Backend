package team.aliens.dms.global.filter

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import team.aliens.dms.global.security.exception.InvalidTokenException
import team.aliens.dms.global.security.token.JwtParser
import team.aliens.dms.global.security.token.JwtProperties
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtFilter(
    private val jwtParser: JwtParser
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = resolvedToken(request)
        SecurityContextHolder.getContext().authentication = jwtParser.getAuthentication(token)

        filterChain.doFilter(request, response)
    }

    private fun resolvedToken(request: HttpServletRequest): String {
        val bearerToken: String? = request.getHeader(JwtProperties.HEADER)

        bearerToken?.let {
            if(bearerToken.startsWith(JwtProperties.PREFIX)) {
                return bearerToken.substring(7)
            }
        }

        throw InvalidTokenException
    }
}