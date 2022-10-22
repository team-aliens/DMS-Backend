package team.aliens.dms.global.security.token

import io.jsonwebtoken.Header
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Component
import team.aliens.dms.domain.auth.dto.TokenResponse
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.auth.spi.JwtPort
import team.aliens.dms.global.security.SecurityProperties
import team.aliens.dms.persistence.auth.model.RefreshTokenEntity
import team.aliens.dms.persistence.auth.repository.RefreshTokenRepository
import java.util.*

@Component
class GenerateJwtAdapter(
    private val securityProperties: SecurityProperties,
    private val refreshTokenRepository: RefreshTokenRepository
) : JwtPort {

    override fun receiveToken(userId: UUID, authority: Authority) = TokenResponse(
        accessToken = generateAccessToken(userId, authority),
        expiredAt = Date(System.currentTimeMillis() + securityProperties.accessExp),
        refreshToken = generateRefreshToken(userId, authority)
    )

    private fun generateAccessToken(userId: UUID, authority: Authority) =
        Jwts.builder()
            .signWith(SignatureAlgorithm.HS512, securityProperties.secretKey)
            .setHeaderParam(Header.JWT_TYPE, JwtProperties.ACCESS)
            .setId(userId.toString())
            .claim(JwtProperties.AUTHORITY, authority.name)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + securityProperties.accessExp))
            .compact()

     private fun generateRefreshToken(userId: UUID, authority: Authority): String {
        val token = Jwts.builder()
            .signWith(SignatureAlgorithm.HS512, securityProperties.secretKey)
            .setHeaderParam(Header.JWT_TYPE, JwtProperties.REFRESH)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + securityProperties.refreshExp))
            .compact()

        refreshTokenRepository.save(
            RefreshTokenEntity(
                token = token,
                userId = userId,
                authority = authority,
                expirationTime = securityProperties.refreshExp / 1000
            )
        )

        return token
    }
}