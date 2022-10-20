package team.aliens.dms.domain.student.usecase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.exception.AuthCodeNotFoundException
import team.aliens.dms.domain.auth.exception.AuthCodeNotMatchedException
import team.aliens.dms.domain.auth.model.AuthCode
import team.aliens.dms.domain.auth.model.EmailType
import team.aliens.dms.domain.student.dto.StudentPasswordInitializationRequest
import team.aliens.dms.domain.student.exception.StudentInfoNotMatchedException
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.StudentCommandUserPort
import team.aliens.dms.domain.student.spi.StudentQueryAuthCodePort
import team.aliens.dms.domain.student.spi.StudentQueryUserPort
import team.aliens.dms.domain.student.spi.StudentSecurityPort
import team.aliens.dms.domain.user.model.User
import java.time.LocalDateTime
import java.util.*

@ExtendWith(SpringExtension::class)
class StudentPasswordInitializationUseCaseTest {

    @MockBean
    private lateinit var queryUserPort: StudentQueryUserPort

    @MockBean
    private lateinit var studentQueryAuthCodePort: StudentQueryAuthCodePort

    @MockBean
    private lateinit var studentCommandUserPort: StudentCommandUserPort

    @MockBean
    private lateinit var studentSecurityPort: StudentSecurityPort

    private lateinit var studentPasswordInitializationUseCase: StudentPasswordInitializationUseCase

    private val accountId = "dlwjddbs13"

    private val email = "dlwjddbs13@naver.com"

    private val name = "이정윤"

    private val code = "AUTH12"

    private val password = "이정윤비번"

    private val user by lazy {
        User(
            id = UUID.randomUUID(),
            schoolId = UUID.randomUUID(),
            accountId = "111111",
            password = password,
            email = email,
            name = name,
            profileImageUrl = "http",
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )
    }

    private val authCode by lazy {
        AuthCode(
            code = code,
            userId = UUID.randomUUID(),
            type = EmailType.PASSWORD,
            expirationTime = 123
        )
    }

    private val request by lazy {
        StudentPasswordInitializationRequest(
            accountId = accountId,
            name = name,
            email = email,
            authCode = code,
            newPassword = password
        )
    }

    private val notMatchedNameRequest by lazy {
        StudentPasswordInitializationRequest(
            accountId = accountId,
            name = "이정윤아님",
            email = email,
            authCode = code,
            newPassword = password
        )
    }

    private val notMatchedEmailRequest by lazy {
        StudentPasswordInitializationRequest(
            accountId = accountId,
            name = name,
            email = "이정윤아님@naver.com",
            authCode = code,
            newPassword = password
        )
    }

    @BeforeEach
    fun setUp() {
        studentPasswordInitializationUseCase = StudentPasswordInitializationUseCase(
            queryUserPort, studentQueryAuthCodePort, studentCommandUserPort, studentSecurityPort
        )
    }

    @Test
    fun `인증코드 일치`() {
        // given
        given(queryUserPort.queryByAccountId(request.accountId))
            .willReturn(user)

        given(studentQueryAuthCodePort.queryAuthCodeByUserId(user.id))
            .willReturn(authCode)

        given(studentSecurityPort.encode(request.newPassword))
            .willReturn(password)

        // when & then
        assertDoesNotThrow {
            studentPasswordInitializationUseCase.execute(request)
        }
    }

    @Test
    fun `유저 존재하지 않음`() {
        // given
        given(queryUserPort.queryByAccountId(request.accountId))
            .willReturn(null)

        // when & then
        assertThrows<StudentNotFoundException> {
            studentPasswordInitializationUseCase.execute(request)
        }
    }



    @Test
    fun `학생 정보 이름 불일치`() {
        // given
        given(queryUserPort.queryByAccountId(request.accountId))
            .willReturn(user)

        // when & then
        assertThrows<StudentInfoNotMatchedException> {
            studentPasswordInitializationUseCase.execute(notMatchedNameRequest)
        }
    }

    @Test
    fun `학생 정보 이메일 불일치`() {
        // given
        given(queryUserPort.queryByAccountId(request.accountId))
            .willReturn(user)

        // when & then
        assertThrows<StudentInfoNotMatchedException> {
            studentPasswordInitializationUseCase.execute(notMatchedEmailRequest)
        }
    }

    @Test
    fun `학생 정보 전부 불일치`() {
        // given
        given(queryUserPort.queryByAccountId(request.accountId))
            .willReturn(user)

        // when & then
        assertThrows<StudentInfoNotMatchedException> {
            studentPasswordInitializationUseCase.execute(request.copy(name = "이정윤아님", email = "이정윤아님@naver.com"))
        }
    }

    @Test
    fun `인증코드 존재하지 않음`() {
        // given
        given(queryUserPort.queryByAccountId(request.accountId))
            .willReturn(user)

        given(studentQueryAuthCodePort.queryAuthCodeByUserId(user.id))
            .willReturn(null)

        // when & then
        assertThrows<AuthCodeNotFoundException> {
            studentPasswordInitializationUseCase.execute(request)
        }
    }

    @Test
    fun `인증코드 불일치`() {
        // given
        given(queryUserPort.queryByAccountId(request.accountId))
            .willReturn(user)

        given(studentQueryAuthCodePort.queryAuthCodeByUserId(user.id))
            .willReturn(authCode)

        // when & then
        assertThrows<AuthCodeNotMatchedException> {
            studentPasswordInitializationUseCase.execute(request.copy(authCode = "222222"))
        }
    }
}