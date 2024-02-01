package team.aliens.dms.global.security.token

import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import team.aliens.dms.domain.auth.dto.TokenResponse
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.auth.model.RefreshToken
import team.aliens.dms.domain.auth.spi.CommandRefreshTokenPort
import team.aliens.dms.domain.auth.spi.JwtPort
import team.aliens.dms.global.security.SecurityProperties
import java.time.LocalDateTime
import java.util.Date
import java.util.UUID

@Component
class GenerateJwtAdapter(
    private val securityProperties: SecurityProperties,
    private val commandRefreshTokenPort: CommandRefreshTokenPort
) : JwtPort {

    override fun receiveToken(userId: UUID, authority: Authority) = TokenResponse(
        accessToken = generateAccessToken(userId, authority),
        accessTokenExpiredAt = LocalDateTime.now().withNano(0).plusSeconds(securityProperties.accessExp.toLong()),
        refreshToken = generateRefreshToken(userId, authority),
        refreshTokenExpiredAt = LocalDateTime.now().withNano(0).plusSeconds(securityProperties.refreshExp.toLong())
    )

    private fun generateAccessToken(userId: UUID, authority: Authority) =
        Jwts.builder()
            .signWith(securityProperties.secretKey)
            .header()
            .add(JwtProperties.JWT_TYPE, JwtProperties.ACCESS)
            .and()
            .id(userId.toString())
            .claim(JwtProperties.AUTHORITY, authority.name)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + securityProperties.accessExp * 1000))
            .compact()

    private fun generateRefreshToken(userId: UUID, authority: Authority): String {
        val token = Jwts.builder()
            .signWith(securityProperties.secretKey)
            .header()
            .add(JwtProperties.JWT_TYPE, JwtProperties.REFRESH)
            .and()
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + securityProperties.refreshExp * 1000))
            .compact()

        val refreshToken = RefreshToken(
            token = token,
            userId = userId,
            authority = authority,
            expirationTime = securityProperties.refreshExp
        )
        commandRefreshTokenPort.save(refreshToken)

        return token
    }
}
