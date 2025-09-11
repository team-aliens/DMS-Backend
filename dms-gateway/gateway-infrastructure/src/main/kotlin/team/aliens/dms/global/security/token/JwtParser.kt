package team.aliens.dms.global.security.token

import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Header
import io.jsonwebtoken.InvalidClaimException
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.auth.model.UserAuthInfo
import team.aliens.dms.global.error.InternalServerError
import team.aliens.dms.global.security.SecurityProperties
import team.aliens.dms.global.security.exception.ExpiredTokenException
import team.aliens.dms.global.security.exception.InvalidTokenException
import java.util.UUID

@Component
class JwtParser(
    private val securityProperties: SecurityProperties
) {

    fun extractUserInfo(token: String): Mono<UserAuthInfo> {

        return getClaims(token)
            .flatMap { claims ->

                if (claims.header[Header.TYPE] != JwtProperties.ACCESS) {
                    Mono.error(InvalidTokenException)
                } else {
                    parseUserInfo(claims.body)
                }
            }
    }

    private fun getClaims(token: String): Mono<Jws<Claims>> {
        return try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(securityProperties.secretKey)
                .build()
                .parseClaimsJws(token)

            Mono.just(claims)
        } catch (e: Exception) {
            val exception = when (e) {
                is InvalidClaimException -> InvalidTokenException
                is ExpiredJwtException -> ExpiredTokenException
                is JwtException -> InvalidTokenException
                else -> InternalServerError
            }
            Mono.error(exception)
        }
    }

    private fun parseUserInfo(claims: Claims): Mono<UserAuthInfo> {
        return try {

            val authority = claims.get(JwtProperties.AUTHORITY, String::class.java)

            val validAuthority = try {
                Authority.valueOf(authority)
            } catch (e: IllegalArgumentException) {
                return Mono.error(InvalidTokenException)
            }

            val userInfo = UserAuthInfo(
                userId = UUID.fromString(claims.id),
                schoolId = UUID.fromString(claims.get(JwtProperties.SCHOOL_ID, String::class.java)),
                authority = validAuthority
            )
            Mono.just(userInfo)
        } catch (e: Exception) {
            Mono.error(InvalidTokenException)
        }
    }
}
