package team.aliens.dms.global.security.token

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Header
import io.jsonwebtoken.InvalidClaimException
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.global.error.InternalServerError
import team.aliens.dms.global.security.SecurityProperties
import team.aliens.dms.global.security.exception.ExpiredTokenException
import team.aliens.dms.global.security.exception.InvalidRoleException
import team.aliens.dms.global.security.exception.InvalidTokenException
import team.aliens.dms.global.security.exception.UnexpectedTokenException
import team.aliens.dms.global.security.principle.ManagerDetailsService
import team.aliens.dms.global.security.principle.StudentDetailsService

@Component
class JwtParser(
    private val securityProperties: SecurityProperties,
    private val studentDetailsService: StudentDetailsService,
    private val managerDetailsService: ManagerDetailsService
) {

    fun getAuthentication(token: String): Authentication {
        val claims = getClaims(token)

        if (claims.header[Header.JWT_TYPE] != JwtProperties.ACCESS) {
            throw InvalidTokenException
        }

        val userDetails = getDetails(claims.body)

        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    private fun getClaims(token: String): Jws<Claims> {
        return try {
            Jwts.parser()
                .setSigningKey(securityProperties.secretKey)
                .parseClaimsJws(token)
        } catch (e: Exception) {
            when (e) {
                is InvalidClaimException -> throw InvalidTokenException
                is ExpiredJwtException -> throw ExpiredTokenException
                is JwtException -> throw UnexpectedTokenException
                else -> throw InternalServerError
            }
        }
    }

    private fun getDetails(body: Claims): UserDetails {
        val authority = body.get(JwtProperties.AUTHORITY, String::class.java)

        return when (authority) {
            Authority.STUDENT.name -> studentDetailsService.loadUserByUsername(body.id)
            Authority.MANAGER.name -> managerDetailsService.loadUserByUsername(body.id)
            else -> throw InvalidRoleException
        }
    }
}
