package team.aliens.dms.global.security.token

import io.jsonwebtoken.*
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import team.aliens.dms.global.exception.InternalServerErrorException
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
        val userDetails = getDetails(claims.body)

        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    private fun getClaims(token: String): Jws<Claims> {
        return try {
            Jwts.parser()
                .setSigningKey(securityProperties.secretKey)
                .parseClaimsJws(token)
        } catch (e: Exception) {
            when(e) {
                is InvalidClaimException -> throw InvalidTokenException
                is ExpiredJwtException -> throw ExpiredTokenException
                is JwtException -> throw UnexpectedTokenException
                else -> throw InternalServerErrorException
            }
        }
    }

    private fun getDetails(body: Claims): UserDetails {
        val authority = body.get("authority", String::class.java)

        return when(authority) {
            "STUDENT" -> studentDetailsService.loadUserByUsername(body.id)
            "MANAGER" -> managerDetailsService.loadUserByUsername(body.id)
            else -> throw InvalidRoleException
        }
    }
}