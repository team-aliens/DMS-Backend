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
    private val schoolId = UUID.randomUUID()

    private val studentSignInRequestStub by lazy {
        SignInRequest(
            accountId = accountId,
            password = password,
            authority = "STUDENT"
        )
    }

    private val managerSignInRequestStub by lazy {
        SignInRequest(
            accountId = accountId,
            password = password,
            authority = "MANAGER"
        )
    }

    private val studentStub by lazy {
        User(
            id = UUID.randomUUID(),
            schoolId = schoolId,
            accountId = accountId,
            password = password,
            email = "email",
            authority = Authority.STUDENT,
            createdAt = null,
            deletedAt = null
        )
    }

    private val managerStub by lazy {
        User(
            id = UUID.randomUUID(),
            schoolId = schoolId,
            accountId = accountId,
            password = password,
            email = "email",
            authority = Authority.MANAGER,
            createdAt = null,
            deletedAt = null
        )
    }

    private val featureStub by lazy {
        AvailableFeature(
            schoolId = schoolId,
            mealService = true,
            noticeService = true,
            pointService = true,
            studyRoomService = false,
            remainService = true
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
        given(queryUserPort.queryUserByAccountId(managerSignInRequestStub.accountId))
            .willReturn(managerStub)

        given(securityPort.isPasswordMatch(managerSignInRequestStub.password, managerStub.password))
            .willReturn(true)

        given(jwtPort.receiveToken(managerStub.id, managerStub.authority))
            .willReturn(tokenResponse)

        given(querySchoolPort.queryAvailableFeaturesBySchoolId(managerStub.schoolId))
            .willReturn(featureStub)

        // when & then
        assertDoesNotThrow {
            signInUseCase.execute(managerSignInRequestStub)
        }
    }

    @Test
    fun `학생일때 로그인 성공`() {
        // given
        given(queryUserPort.queryUserByAccountId(studentSignInRequestStub.accountId))
            .willReturn(studentStub)

        given(securityPort.isPasswordMatch(studentSignInRequestStub.password, studentStub.password))
            .willReturn(true)

        given(jwtPort.receiveToken(studentStub.id, Authority.STUDENT))
            .willReturn(tokenResponse)

        given(querySchoolPort.queryAvailableFeaturesBySchoolId(studentStub.schoolId))
            .willReturn(featureStub)

        // when & then
        assertDoesNotThrow {
            signInUseCase.execute(studentSignInRequestStub)
        }
    }

    @Test
    fun `유저를 찾을 수 없음`() {
        // given
        given(queryUserPort.queryUserByAccountId(managerSignInRequestStub.accountId))
            .willReturn(null)

        // when & then
        assertThrows<UserNotFoundException> {
            signInUseCase.execute(managerSignInRequestStub)
        }
    }

    @Test
    fun `비밀번호가 틀림`() {
        // given
        given(queryUserPort.queryUserByAccountId(studentSignInRequestStub.accountId))
            .willReturn(studentStub)

        given(securityPort.isPasswordMatch(studentSignInRequestStub.password, studentStub.password))
            .willReturn(false)

        // when & then
        assertThrows<PasswordMismatchException> {
            signInUseCase.execute(studentSignInRequestStub)
        }
    }

    @Test
    fun `이용 가능한 기능이 존재하지 않음`() {
        // given
        given(queryUserPort.queryUserByAccountId(studentSignInRequestStub.accountId))
            .willReturn(studentStub)

        given(securityPort.isPasswordMatch(studentSignInRequestStub.password, studentStub.password))
            .willReturn(true)

        given(jwtPort.receiveToken(studentStub.id, studentStub.authority))
            .willReturn(tokenResponse)

        given(querySchoolPort.queryAvailableFeaturesBySchoolId(studentStub.schoolId))
            .willReturn(null)

        // when & then
        assertThrows<FeatureNotFoundException> {
            signInUseCase.execute(studentSignInRequestStub)
        }
    }
}
