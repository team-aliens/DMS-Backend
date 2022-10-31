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
import team.aliens.dms.domain.auth.spi.AuthQueryUserPort
import team.aliens.dms.domain.auth.spi.AuthSecurityPort
import team.aliens.dms.domain.auth.spi.JwtPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import team.aliens.dms.domain.user.service.CheckUserAuthority
import java.time.LocalDateTime
import java.util.*

@ExtendWith(SpringExtension::class)
class SignInUseCaseTests {

    @MockBean
    private lateinit var securityPort: AuthSecurityPort

    @MockBean
    private lateinit var queryUserPort: AuthQueryUserPort

    @MockBean
    private lateinit var jwtPort: JwtPort

    @MockBean
    private lateinit var checkUserAuthority: CheckUserAuthority

    private lateinit var signInUseCase: SignInUseCase

    @BeforeEach
    fun setUp() {
        signInUseCase = SignInUseCase(
            securityPort, queryUserPort, jwtPort, checkUserAuthority
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
            name = "name",
            profileImageUrl = "http://~~",
            createdAt = null,
            deletedAt = null
        )
    }

    private val tokenResponse = TokenResponse(
            accessToken = "Bearer dga",
            expiredAt = LocalDateTime.now(),
            refreshToken = "Bearer sdalkgmsalkgmsa"
    )

    @Test
    fun `매니저일때 로그인 성공`() {
        // given
        given(queryUserPort.queryUserByAccountId(requestStub.accountId))
            .willReturn(userStub)

        given(securityPort.isPasswordMatch(requestStub.password, userStub.password))
            .willReturn(true)

        given(checkUserAuthority.execute(userStub.id))
            .willReturn(Authority.MANAGER)

        given(jwtPort.receiveToken(userStub.id, Authority.MANAGER))
            .willReturn(tokenResponse)

        // when then
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

        given(checkUserAuthority.execute(userStub.id))
            .willReturn(Authority.STUDENT)

        given(jwtPort.receiveToken(userStub.id, Authority.STUDENT))
            .willReturn(tokenResponse)

        // when then
        assertDoesNotThrow {
            signInUseCase.execute(requestStub)
        }
    }

    @Test
    fun `유저를 찾을 수 없음`() {
        // given
        given(queryUserPort.queryUserByAccountId(requestStub.accountId))
            .willReturn(null)

        // when then
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

        // when then
        assertThrows<PasswordMismatchException> {
            signInUseCase.execute(requestStub)
        }
    }
}