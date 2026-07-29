package team.aliens.dms.global.security.token

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Header
import io.jsonwebtoken.InvalidClaimException
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.auth.model.PassportUser
import team.aliens.dms.global.error.InternalServerError
import team.aliens.dms.global.security.SecurityProperties
import team.aliens.dms.global.security.exception.ExpiredTokenException
import team.aliens.dms.global.security.exception.InvalidTokenException
import java.util.UUID

@Component
class JwtParser(
    private val securityProperties: SecurityProperties
) {

    fun extractUserInfo(token: String): PassportUser {
        val claims = getClaims(token)

        if (claims.header[Header.TYPE] != JwtProperties.ACCESS) {
            throw InvalidTokenException
        }

        return parseUserInfo(claims.body)
    }

    private fun getClaims(token: String): Jws<Claims> {
        return try {
            Jwts.parserBuilder()
                .setSigningKey(securityProperties.secretKey)
                .build()
                .parseClaimsJws(token)
        } catch (e: ExpiredJwtException) {
            throw ExpiredTokenException
        } catch (e: InvalidClaimException) {
            throw InvalidTokenException
        } catch (e: JwtException) {
            throw InvalidTokenException
        } catch (e: Exception) {
            throw InternalServerError
        }
    }

    private fun parseUserInfo(claims: Claims): PassportUser {
        val authorityValue = claims.get(JwtProperties.AUTHORITY, String::class.java)
            ?: throw InvalidTokenException

        val authority = try {
            Authority.valueOf(authorityValue)
        } catch (e: IllegalArgumentException) {
            throw InvalidTokenException
        }

        return try {
            PassportUser(
                id = UUID.fromString(claims.id),
                schoolId = UUID.fromString(claims.get(JwtProperties.SCHOOL_ID, String::class.java)),
                authority = authority
            )
        } catch (e: Exception) {
            throw InvalidTokenException
        }
    }
}
