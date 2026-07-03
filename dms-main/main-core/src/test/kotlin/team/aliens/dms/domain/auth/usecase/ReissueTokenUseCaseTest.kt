package team.aliens.dms.domain.auth.usecase

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import team.aliens.dms.domain.auth.dto.TokenResponse
import team.aliens.dms.domain.auth.service.AuthService
import team.aliens.dms.domain.auth.spi.JwtPort
import team.aliens.dms.domain.auth.stub.createRefreshToken
import team.aliens.dms.domain.school.service.SchoolService
import team.aliens.dms.domain.user.service.UserService
import team.aliens.dms.domain.user.stub.createUserStub
import java.time.LocalDateTime
import java.util.UUID

class ReissueTokenUseCaseTest : DescribeSpec({

    isolationMode = IsolationMode.InstancePerLeaf

    val authService = mockk<AuthService>()
    val userService = mockk<UserService>()
    val schoolService = mockk<SchoolService>()
    val jwtPort = mockk<JwtPort>()
    val reissueTokenUseCase = ReissueTokenUseCase(jwtPort, userService, schoolService, authService)

    describe("execute") {

        context("유효한 refresh token으로 재발급을 요청하면") {

            val token = "refresh-token"
            val userId = UUID.randomUUID()
            val schoolId = UUID.randomUUID()

            val queryToken = createRefreshToken(token = token, userId = userId)
            val user = createUserStub(id = userId, schoolId = schoolId)

            // 수정된 동작: 재발급 시 refresh token은 그대로(token) 두고 access token만 새로 발급
            val tokenResponse = TokenResponse(
                accessToken = "new-access-token",
                accessTokenExpiredAt = LocalDateTime.now().plusHours(1),
                refreshToken = token,
                refreshTokenExpiredAt = LocalDateTime.now().plusWeeks(2)
            )

            every { authService.getRefreshTokenByToken(token) } returns queryToken
            every { userService.queryUserById(userId) } returns user
            every { jwtPort.reissueAccessToken(queryToken, schoolId) } returns tokenResponse
            every { schoolService.getAvailableFeaturesBySchoolId(schoolId) } returns mockk(relaxed = true)

            it("access token만 새로 발급하고 refresh token은 기존 값을 그대로 반환한다") {
                val result = reissueTokenUseCase.execute(token)

                result.accessToken shouldBe "new-access-token"

                result.refreshToken shouldBe token
            }
        }
    }
})
