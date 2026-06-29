package team.aliens.dms.global.security.token

import io.jsonwebtoken.Header
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
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
    private val commandRefreshTokenPort: CommandRefreshTokenPort,
) : JwtPort {

    override fun receiveToken(userId: UUID, authority: Authority, schoolId: UUID) = TokenResponse(
        accessToken = generateAccessToken(userId, authority, schoolId),
        accessTokenExpiredAt = LocalDateTime.now().withNano(0).plusSeconds(securityProperties.accessExp.toLong()),
        refreshToken = generateRefreshToken(userId, authority, schoolId),
        refreshTokenExpiredAt = LocalDateTime.now().withNano(0).plusSeconds(securityProperties.refreshExp.toLong())
    )

    override fun reissueAccessToken(refreshToken: RefreshToken, schoolId: UUID): TokenResponse {
        // refresh token 값은 유지하고 TTL(만료 시간)만 갱신하여 슬라이딩 세션을 유지한다.
        commandRefreshTokenPort.save(
            refreshToken.copy(expirationTime = securityProperties.refreshExp)
        )

        return TokenResponse(
            accessToken = generateAccessToken(refreshToken.userId, refreshToken.authority, schoolId),
            accessTokenExpiredAt = LocalDateTime.now().withNano(0).plusSeconds(securityProperties.accessExp.toLong()),
            refreshToken = refreshToken.token,
            refreshTokenExpiredAt = LocalDateTime.now().withNano(0).plusSeconds(securityProperties.refreshExp.toLong())
        )
    }

    private fun generateAccessToken(userId: UUID, authority: Authority, schoolId: UUID) =
        Jwts.builder()
            .signWith(securityProperties.secretKey, SignatureAlgorithm.HS512)
            .setHeaderParam(Header.TYPE, JwtProperties.ACCESS)
            .setId(userId.toString())
            .claim(JwtProperties.AUTHORITY, authority.name)
            .claim(JwtProperties.SCHOOL_ID, schoolId.toString())
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + securityProperties.accessExp * 1000))
            .compact()

    private fun generateRefreshToken(userId: UUID, authority: Authority, schoolId: UUID): String {
        val token = Jwts.builder()
            .signWith(securityProperties.secretKey, SignatureAlgorithm.HS512)
            .setHeaderParam(Header.TYPE, JwtProperties.REFRESH)
            .setId(userId.toString())
            .claim(JwtProperties.SCHOOL_ID, schoolId.toString())
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + securityProperties.refreshExp * 1000))
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
