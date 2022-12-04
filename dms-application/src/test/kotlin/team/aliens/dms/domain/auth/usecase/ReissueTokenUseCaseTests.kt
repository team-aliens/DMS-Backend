package team.aliens.dms.domain.auth.usecase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.dto.TokenResponse
import team.aliens.dms.domain.auth.exception.RefreshTokenNotFoundException
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.auth.model.RefreshToken
import team.aliens.dms.domain.auth.spi.AuthQuerySchoolPort
import team.aliens.dms.domain.auth.spi.AuthQueryUserPort
import team.aliens.dms.domain.auth.spi.JwtPort
import team.aliens.dms.domain.auth.spi.QueryRefreshTokenPort
import team.aliens.dms.domain.school.exception.FeatureNotFoundException
import team.aliens.dms.domain.school.model.AvailableFeature
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import java.time.LocalDateTime
import java.util.UUID

@ExtendWith(SpringExtension::class)
class ReissueTokenUseCaseTests {

    @MockBean
    private lateinit var jwtPort: JwtPort

    @MockBean
    private lateinit var queryRefreshTokenPort: QueryRefreshTokenPort

    @MockBean
    private lateinit var queryUserPort: AuthQueryUserPort

    @MockBean
    private lateinit var querySchoolPort: AuthQuerySchoolPort

    private lateinit var reissueTokenUseCase: ReissueTokenUseCase

    @BeforeEach
    fun setUp() {
        reissueTokenUseCase = ReissueTokenUseCase(
            jwtPort, queryRefreshTokenPort, queryUserPort, querySchoolPort
        )
    }

    private val token = "rlkemagkldamgl"
    private val userId = UUID.randomUUID()

    private val refreshTokenStub by lazy {
        RefreshToken(
            token = token,
            userId = userId,
            authority = Authority.MANAGER,
            expirationTime = 1800
        )
    }

    private val userStub by lazy {
        User(
            id = UUID.randomUUID(),
            schoolId = UUID.randomUUID(),
            accountId = "accountId",
            password = "password",
            email = "email",
            authority = Authority.STUDENT,
            createdAt = null,
            deletedAt = null
        )
    }

    private val featureStub by lazy {
        AvailableFeature(
            schoolId = userStub.schoolId,
            mealService = true,
            noticeService = true,
            pointService = true
        )
    }

    private val tokenResponseStub by lazy {
        TokenResponse(
            accessToken = "Bearer dalkmas",
            accessTokenExpiredAt = LocalDateTime.now(),
            refreshToken = "Bearer akldsgmaslkdgmadk",
            refreshTokenExpiredAt = LocalDateTime.now()
        )
    }

    @Test
    fun `토큰 재발급 성공`() {
        //given
        given(queryRefreshTokenPort.queryRefreshTokenByToken(token))
            .willReturn(refreshTokenStub)

        given(jwtPort.receiveToken(refreshTokenStub.userId, refreshTokenStub.authority))
            .willReturn(tokenResponseStub)

        given(queryUserPort.queryUserById(userId))
            .willReturn(userStub)

        given(querySchoolPort.queryAvailableFeaturesBySchoolId(userStub.schoolId))
            .willReturn(featureStub)

        //when & then
        assertDoesNotThrow {
            reissueTokenUseCase.execute(token)
        }
    }

    @Test
    fun `리프레시 토큰을 찾을 수 없음`() {
        //given
        given(queryRefreshTokenPort.queryRefreshTokenByToken(token))
            .willReturn(null)

        //when & then
        assertThrows<RefreshTokenNotFoundException> {
            reissueTokenUseCase.execute(token)
        }
    }

    @Test
    fun `유저를 찾을 수 없음`() {
        // given
        given(queryRefreshTokenPort.queryRefreshTokenByToken(token))
            .willReturn(refreshTokenStub)

        given(jwtPort.receiveToken(refreshTokenStub.userId, refreshTokenStub.authority))
            .willReturn(tokenResponseStub)

        given(queryUserPort.queryUserById(userId))
            .willReturn(null)

        // when & then
        assertThrows<UserNotFoundException> {
            reissueTokenUseCase.execute(token)
        }
    }

    @Test
    fun `이용 가능한 기능이 존재하지 않음`() {
        //given
        given(queryRefreshTokenPort.queryRefreshTokenByToken(token))
            .willReturn(refreshTokenStub)

        given(jwtPort.receiveToken(refreshTokenStub.userId, refreshTokenStub.authority))
            .willReturn(tokenResponseStub)

        given(queryUserPort.queryUserById(userId))
            .willReturn(userStub)

        given(querySchoolPort.queryAvailableFeaturesBySchoolId(userStub.schoolId))
            .willReturn(null)

        // when & then
        assertThrows<FeatureNotFoundException> {
            reissueTokenUseCase.execute(token)
        }
    }
}