package team.aliens.dms.global.filter

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.auth.model.Passport
import team.aliens.dms.global.security.SecurityPaths
import team.aliens.dms.global.security.exception.InvalidTokenException
import team.aliens.dms.global.security.passport.PassportSecurityProperties
import team.aliens.dms.global.security.passport.PassportValidator
import team.aliens.dms.global.security.principle.ManagerDetails
import team.aliens.dms.global.security.principle.StudentDetails

class PassportFilter(
    private val passportValidator: PassportValidator,
    private val objectMapper: ObjectMapper
) : OncePerRequestFilter() {

    private val pathMatcher = AntPathMatcher()

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        val path = request.requestURI
        val shouldSkip = SecurityPaths.PERMIT_ALL_PATHS.any { permitPath ->
            pathMatcher.match(permitPath, path)
        }
        return shouldSkip
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val passportHeader = request.getHeader(PassportSecurityProperties.PASSPORT_HEADER)

        val passport = resolvePassport(passportHeader)

        SecurityContextHolder.clearContext()

        passport.let {
            val authentication = createAuthentication(it)
            SecurityContextHolder.getContext().authentication = authentication
        }

        filterChain.doFilter(request, response)
    }

    private fun resolvePassport(passportHeader: String?): Passport {
        if (passportHeader.isNullOrBlank()) {
            throw InvalidTokenException
        }

        return try {
            val passport = objectMapper.readValue(passportHeader, Passport::class.java)
            if (!passportValidator.validate(passport)) {
                throw InvalidTokenException
            }
            passport
        } catch (e: Exception) {
            throw InvalidTokenException
        }
    }

    private fun createAuthentication(passport: Passport): UsernamePasswordAuthenticationToken {
        val authorities = listOf(SimpleGrantedAuthority(passport.user.authority.name))

        val details = when (passport.user.authority) {
            Authority.STUDENT -> StudentDetails(
                userId = passport.user.id,
                schoolId = passport.user.schoolId,
                authority = passport.user.authority
            )
            Authority.MANAGER -> ManagerDetails(
                userId = passport.user.id,
                schoolId = passport.user.schoolId,
                authority = passport.user.authority
            )
        }

        return UsernamePasswordAuthenticationToken(details, null, authorities)
    }
}
