package team.aliens.dms.domain.auth.usecase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.dto.SignInRequest
import team.aliens.dms.domain.auth.dto.TokenResponse
import team.aliens.dms.domain.auth.exception.PasswordMismatchException
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.auth.spi.AuthQuerySchoolPort
import team.aliens.dms.domain.auth.spi.AuthQueryUserPort
import team.aliens.dms.domain.auth.spi.AuthSecurityPort
import team.aliens.dms.domain.auth.spi.JwtPort
import team.aliens.dms.domain.school.exception.FeatureNotFoundException
import team.aliens.dms.domain.school.model.AvailableFeature
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import java.time.LocalDateTime
import java.util.UUID

@ExtendWith(SpringExtension::class)
class SignInUseCaseTests {

    @MockBean
    private lateinit var securityPort: AuthSecurityPort

    @MockBean
    private lateinit var queryUserPort: AuthQueryUserPort

    @MockBean
    private lateinit var querySchoolPort: AuthQuerySchoolPort

    @MockBean
    private lateinit var jwtPort: JwtPort

    private lateinit var signInUseCase: SignInUseCase

    @BeforeEach
    fun setUp() {
        signInUseCase = SignInUseCase(
            securityPort, queryUserPort, querySchoolPort, jwtPort
        )
    }

    private val accountId = "accountId"
    private val password = "password"

    private val requestStub by lazy {
        SignInRequest(
            accountId = accountId,
            password = password
        )
    }

    private val userStub by lazy {
        User(
            id = UUID.randomUUID(),
            schoolId = UUID.randomUUID(),
            accountId = accountId,
            password = password,
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

    private val tokenResponse = TokenResponse(
        accessToken = "Bearer dga",
        accessTokenExpiredAt = LocalDateTime.now(),
        refreshToken = "Bearer sdalkgmsalkgmsa",
        refreshTokenExpiredAt = LocalDateTime.now()
    )

    @Test
    fun `매니저일때 로그인 성공`() {
        // given
        given(queryUserPort.queryUserByAccountId(requestStub.accountId))
            .willReturn(userStub)

        given(securityPort.isPasswordMatch(requestStub.password, userStub.password))
            .willReturn(true)

        given(jwtPort.receiveToken(userStub.id, userStub.authority))
            .willReturn(tokenResponse)

        given(querySchoolPort.queryAvailableFeaturesBySchoolId(userStub.schoolId))
            .willReturn(featureStub)

        // when & then
        assertDoesNotThrow {
            signInUseCase.execute(requestStub)
        }
    }

    @Test
    fun `학생일때 로그인 성공`() {
        // given
        given(queryUserPort.queryUserByAccountId(requestStub.accountId))
            .willReturn(userStub)

        given(securityPort.isPasswordMatch(requestStub.password, userStub.password))
            .willReturn(true)

        given(jwtPort.receiveToken(userStub.id, Authority.STUDENT))
            .willReturn(tokenResponse)

        given(querySchoolPort.queryAvailableFeaturesBySchoolId(userStub.schoolId))
            .willReturn(featureStub)

        // when & then
        assertDoesNotThrow {
            signInUseCase.execute(requestStub)
        }
    }

    @Test
    fun `유저를 찾을 수 없음`() {
        // given
        given(queryUserPort.queryUserByAccountId(requestStub.accountId))
            .willReturn(null)

        // when & then
        assertThrows<UserNotFoundException> {
            signInUseCase.execute(requestStub)
        }
    }

    @Test
    fun `비밀번호가 틀림`() {
        // given
        given(queryUserPort.queryUserByAccountId(requestStub.accountId))
            .willReturn(userStub)

        given(securityPort.isPasswordMatch(requestStub.password, userStub.password))
            .willReturn(false)

        // when & then
        assertThrows<PasswordMismatchException> {
            signInUseCase.execute(requestStub)
        }
    }

    @Test
    fun `이용 가능한 기능이 존재하지 않음`() {
        // given
        given(queryUserPort.queryUserByAccountId(requestStub.accountId))
            .willReturn(userStub)

        given(securityPort.isPasswordMatch(requestStub.password, userStub.password))
            .willReturn(true)

        given(jwtPort.receiveToken(userStub.id, userStub.authority))
            .willReturn(tokenResponse)

        given(querySchoolPort.queryAvailableFeaturesBySchoolId(userStub.schoolId))
            .willReturn(null)

        // when & then
        assertThrows<FeatureNotFoundException> {
            signInUseCase.execute(requestStub)
        }
    }
}