package team.aliens.dms.domain.auth.usecase

import team.aliens.dms.domain.auth.dto.TokenResponse
import team.aliens.dms.domain.auth.exception.RefreshTokenNotFoundException
import team.aliens.dms.domain.auth.spi.JwtPort
import team.aliens.dms.domain.auth.spi.QueryRefreshTokenPort
import team.aliens.dms.global.annotation.UseCase

@UseCase
class ReissueTokenUseCase(
        private val queryRefreshTokenPort: QueryRefreshTokenPort,
        private val jwtPort: JwtPort
) {

    fun execute(refreshToken: String): TokenResponse {
        val token = queryRefreshTokenPort.queryRefreshTokenByToken(refreshToken)
                ?: throw RefreshTokenNotFoundException

        return jwtPort.receiveToken(token.userId, token.authority)
    }
}