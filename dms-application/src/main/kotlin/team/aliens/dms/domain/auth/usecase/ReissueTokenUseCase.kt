package team.aliens.dms.domain.auth.usecase

import team.aliens.dms.domain.auth.dto.ReissueResponse
import team.aliens.dms.domain.auth.exception.RefreshTokenNotFoundException
import team.aliens.dms.domain.auth.spi.JwtPort
import team.aliens.dms.domain.auth.spi.QueryRefreshTokenPort
import team.aliens.dms.common.annotation.UseCase

@UseCase
class ReissueTokenUseCase(
    private val jwtPort: JwtPort,
    private val queryRefreshTokenPort: QueryRefreshTokenPort
) {

    fun execute(token: String): ReissueResponse {
        val queryToken = queryRefreshTokenPort.queryRefreshTokenByToken(token) ?: throw RefreshTokenNotFoundException

        val (accessToken, expiredAt, refreshToken) = jwtPort.receiveToken(queryToken.userId, queryToken.authority)

        return ReissueResponse(
            accessToken = accessToken,
            expiredAt = expiredAt,
            refreshToken = refreshToken,
            features = ReissueResponse.Features(
                // TODO 서비스 관리 테이블 필요
                mealService = true,
                noticeService = true,
                pointService = true
            )
        )
    }
}