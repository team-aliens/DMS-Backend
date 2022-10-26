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
import team.aliens.dms.domain.auth.spi.AuthQueryStudentPort
import team.aliens.dms.domain.auth.spi.AuthQueryUserPort
import team.aliens.dms.domain.auth.spi.JwtPort
import team.aliens.dms.domain.auth.spi.SecurityPort
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import java.time.LocalDateTime
import java.util.*

@ExtendWith(SpringExtension::class)
class SignInUseCaseTest {

    @MockBean
    private lateinit var securityPort: SecurityPort

    @MockBean
    private lateinit var queryUserPort: AuthQueryUserPort

    @MockBean
    private lateinit var queryStudentPort: AuthQueryStudentPort

    @MockBean
    private lateinit var jwtPort: JwtPort

    private lateinit var signInUseCase: SignInUseCase

    @BeforeEach
    fun setUp() {
        signInUseCase = SignInUseCase(
            securityPort,
            queryUserPort,
            queryStudentPort,
            jwtPort
        )
    }

    private val accountId = "accountId"
    private val password = "password"

    private val request by lazy {
        SignInRequest(
            accountId = accountId,
            password = password
        )
    }

    private val user by lazy {
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

    private val tokenResponse =
        TokenResponse(
            accessToken = "bearer dga",
            expiredAt = LocalDateTime.now(),
            refreshToken = "bearer sdalkgmsalkgmsa"
        )


    @Test
    fun `매니저일때 로그인 성공`() {
        // given
        given(queryUserPort.queryUserByAccountId(request.accountId))
            .willReturn(user)
        given(securityPort.isPasswordMatch(request.password, user.password))
            .willReturn(true)
        given(queryStudentPort.queryStudentById(user.id))
            .willReturn(null)
        given(jwtPort.receiveToken(user.id, Authority.MANAGER))
            .willReturn(tokenResponse)

        // when then
        assertDoesNotThrow {
            signInUseCase.execute(request)
        }
    }

    @Test
    fun `학생일때 로그인 성공`() {
        // given
        given(queryUserPort.queryUserByAccountId(request.accountId))
            .willReturn(user)
        given(securityPort.isPasswordMatch(request.password, user.password))
            .willReturn(true)
        given(queryStudentPort.queryStudentById(user.id))
            .willReturn(Student(
                studentId = user.id,
                roomNumber = 1,
                schoolId = UUID.randomUUID(),
                grade = 2,
                classRoom = 1,
                number = 17
            ))
        given(jwtPort.receiveToken(user.id, Authority.STUDENT))
            .willReturn(tokenResponse)

        // when then
        assertDoesNotThrow {
            signInUseCase.execute(request)
        }
    }

    @Test
    fun `유저를 찾을 수 없음`() {
        // given
        given(queryUserPort.queryUserByAccountId(request.accountId))
            .willReturn(null)

        // when then
        assertThrows<UserNotFoundException> {
            signInUseCase.execute(request)
        }
    }

    @Test
    fun `비밀번호가 틀림`() {
        // given
        given(queryUserPort.queryUserByAccountId(request.accountId))
            .willReturn(user)
        given(securityPort.isPasswordMatch(request.password, user.password))
            .willReturn(false)

        // when then
        assertThrows<PasswordMismatchException> {
            signInUseCase.execute(request)
        }
    }
}