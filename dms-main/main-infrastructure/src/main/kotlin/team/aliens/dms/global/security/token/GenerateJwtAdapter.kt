package team.aliens.dms.global.security.token

import io.jsonwebtoken.Header
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import team.aliens.dms.domain.auth.dto.TokenResponse
import team.aliens.dms.domain.auth.exception.RefreshTokenNotFoundException
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
    private val jwtParser: JwtParser,
) : JwtPort {

    override fun receiveToken(userId: UUID, authority: Authority, schoolId: UUID): TokenResponse {
        val refreshToken = commandRefreshTokenPort.save(
            RefreshToken(
                token = generateRefreshToken(userId, schoolId),
                userId = userId,
                authority = authority,
                expirationTime = securityProperties.refreshExp
            )
        )

        return createTokenResponse(refreshToken, schoolId)
    }

    override fun reissueToken(refreshToken: RefreshToken, schoolId: UUID): TokenResponse {
        val rotatedRefreshToken = commandRefreshTokenPort.rotateRefreshToken(
            currentToken = refreshToken.token,
            newRefreshToken = refreshToken.copy(
                token = generateRefreshToken(refreshToken.userId, schoolId),
                expirationTime = securityProperties.refreshExp
            )
        ) ?: throw RefreshTokenNotFoundException

        return createTokenResponse(rotatedRefreshToken, schoolId)
    }

    // 위조·만료된 토큰도 없는 토큰과 같은 404로 응답한다
    override fun getUserIdByRefreshToken(refreshToken: String): UUID =
        jwtParser.extractRefreshTokenUserId(refreshToken) ?: throw RefreshTokenNotFoundException

    private fun createTokenResponse(refreshToken: RefreshToken, schoolId: UUID): TokenResponse {
        val now = LocalDateTime.now().withNano(0)

        return TokenResponse(
            accessToken = generateAccessToken(refreshToken.userId, refreshToken.authority, schoolId),
            accessTokenExpiredAt = now.plusSeconds(securityProperties.accessExp.toLong()),
            refreshToken = refreshToken.token,
            refreshTokenExpiredAt = now.plusSeconds(refreshToken.expirationTime.toLong())
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

    private fun generateRefreshToken(userId: UUID, schoolId: UUID) =
        Jwts.builder()
            .signWith(securityProperties.secretKey, SignatureAlgorithm.HS512)
            .setHeaderParam(Header.TYPE, JwtProperties.REFRESH)
            .setId(userId.toString())
            .claim(JwtProperties.SCHOOL_ID, schoolId.toString())
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + securityProperties.refreshExp * 1000))
            .compact()
}
