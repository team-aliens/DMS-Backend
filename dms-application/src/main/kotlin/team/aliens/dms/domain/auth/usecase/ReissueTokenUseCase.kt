package team.aliens.dms.domain.auth.usecase

import team.aliens.dms.domain.auth.dto.ReissueTokenResponse
import team.aliens.dms.domain.auth.exception.RefreshTokenNotFoundException
import team.aliens.dms.domain.auth.spi.JwtPort
import team.aliens.dms.domain.auth.spi.QueryRefreshTokenPort
import team.aliens.dms.global.annotation.UseCase

@UseCase
class ReissueTokenUseCase(
        private val queryRefreshTokenPort: QueryRefreshTokenPort,
        private val jwtPort: JwtPort
) {

    fun execute(token: String): ReissueTokenResponse {
        val queryToken = queryRefreshTokenPort.queryRefreshTokenByToken(token)
                ?: throw RefreshTokenNotFoundException

        val (accessToken, expiredAt, refreshToken) = jwtPort.receiveToken(queryToken.userId, queryToken.authority)

        return ReissueTokenResponse(
            accessToken = accessToken,
            expiredAt = expiredAt,
            refreshToken = refreshToken,
            features = ReissueTokenResponse.Features(
                // TODO 서비스 관리 테이블 필요

                mealService = true,
                noticeService = true,
                pointService = true
            )
        )
    }
}