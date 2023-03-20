package team.aliens.dms.domain.student.usecase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.exception.AuthCodeMismatchException
import team.aliens.dms.domain.auth.exception.AuthCodeNotFoundException
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.auth.model.EmailType
import team.aliens.dms.domain.auth.stub.createAuthCodeStub
import team.aliens.dms.domain.student.dto.ResetStudentPasswordRequest
import team.aliens.dms.domain.student.exception.StudentInfoMismatchException
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.student.spi.StudentCommandUserPort
import team.aliens.dms.domain.student.spi.StudentQueryAuthCodePort
import team.aliens.dms.domain.student.spi.StudentQueryUserPort
import team.aliens.dms.domain.student.spi.StudentSecurityPort
import team.aliens.dms.domain.student.stub.createStudentStub
import team.aliens.dms.domain.user.exception.InvalidRoleException
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.service.CheckUserAuthority
import team.aliens.dms.domain.user.stub.createUserStub

@ExtendWith(SpringExtension::class)
class ResetStudentPasswordUseCaseTests {

    @MockBean
    private lateinit var queryUserPort: StudentQueryUserPort

    @MockBean
    private lateinit var queryStudentPort: QueryStudentPort

    @MockBean
    private lateinit var queryAuthCodePort: StudentQueryAuthCodePort

    @MockBean
    private lateinit var commandUserPort: StudentCommandUserPort

    @MockBean
    private lateinit var securityPort: StudentSecurityPort

    @MockBean
    private lateinit var checkUserAuthority: CheckUserAuthority

    private lateinit var resetStudentPasswordUseCase: ResetStudentPasswordUseCase

    @BeforeEach
    fun setUp() {
        resetStudentPasswordUseCase = ResetStudentPasswordUseCase(
            queryUserPort,
            queryStudentPort,
            queryAuthCodePort,
            commandUserPort,
            securityPort,
            checkUserAuthority
        )
    }

    private val accountId = "dlwjddbs13"
    private val email = "dlwjddbs13@naver.com"
    private val name = "이정윤"
    private val code = "AUTH12"
    private val password = "이정윤비번"

    private val userStub by lazy {
        createUserStub(
            password = password,
            email = email
        )
    }

    private val studentStub by lazy {
        createStudentStub(
            id = userStub.id,
            name = name
        )
    }

    private val authCodeStub by lazy {
        createAuthCodeStub(
            code = code,
            type = EmailType.PASSWORD
        )
    }

    private val requestStub by lazy {
        ResetStudentPasswordRequest(
            accountId = accountId,
            name = name,
            email = email,
            authCode = code,
            newPassword = password
        )
    }

    private val notMatchedNameRequestStub by lazy {
        ResetStudentPasswordRequest(
            accountId = accountId,
            name = "이정윤아님",
            email = email,
            authCode = code,
            newPassword = password
        )
    }

    private val notMatchedEmailRequestStub by lazy {
        ResetStudentPasswordRequest(
            accountId = accountId,
            name = name,
            email = "이정윤아님@naver.com",
            authCode = code,
            newPassword = password
        )
    }

    @Test
    fun `인증코드 일치`() {
        // given
        given(queryUserPort.queryUserByAccountId(requestStub.accountId))
            .willReturn(userStub)

        given(queryStudentPort.queryStudentById(studentStub.id))
            .willReturn(studentStub)

        given(checkUserAuthority.execute(userStub.id))
            .willReturn(Authority.STUDENT)

        given(queryAuthCodePort.queryAuthCodeByEmail(userStub.email))
            .willReturn(authCodeStub)

        given(securityPort.encodePassword(requestStub.newPassword))
            .willReturn(password)

        // when & then
        assertDoesNotThrow {
            resetStudentPasswordUseCase.execute(requestStub)
        }
    }

    @Test
    fun `유저 존재하지 않음`() {
        // given
        given(queryUserPort.queryUserByAccountId(requestStub.accountId))
            .willReturn(null)

        // when & then
        assertThrows<UserNotFoundException> {
            resetStudentPasswordUseCase.execute(requestStub)
        }
    }

    @Test
    fun `유저 권한 불일치`() {
        // given
        given(queryUserPort.queryUserByAccountId(requestStub.accountId))
            .willReturn(userStub)

        given(queryStudentPort.queryStudentById(studentStub.id))
            .willReturn(studentStub)

        given(checkUserAuthority.execute(userStub.id))
            .willReturn(Authority.MANAGER)

        // when & then
        assertThrows<InvalidRoleException> {
            resetStudentPasswordUseCase.execute(requestStub)
        }
    }

    @Test
    fun `학생 정보 이름 불일치`() {
        // given
        given(queryUserPort.queryUserByAccountId(requestStub.accountId))
            .willReturn(userStub)

        given(queryStudentPort.queryStudentById(studentStub.id))
            .willReturn(studentStub)

        given(checkUserAuthority.execute(userStub.id))
            .willReturn(Authority.STUDENT)

        // when & then
        assertThrows<StudentInfoMismatchException> {
            resetStudentPasswordUseCase.execute(notMatchedNameRequestStub)
        }
    }

    @Test
    fun `학생 정보 이메일 불일치`() {
        // given
        given(queryUserPort.queryUserByAccountId(requestStub.accountId))
            .willReturn(userStub)

        given(queryStudentPort.queryStudentById(studentStub.id))
            .willReturn(studentStub)

        given(checkUserAuthority.execute(userStub.id))
            .willReturn(Authority.STUDENT)

        // when & then
        assertThrows<StudentInfoMismatchException> {
            resetStudentPasswordUseCase.execute(notMatchedEmailRequestStub)
        }
    }

    @Test
    fun `학생 정보 전부 불일치`() {
        // given
        given(queryUserPort.queryUserByAccountId(requestStub.accountId))
            .willReturn(userStub)

        given(queryStudentPort.queryStudentById(studentStub.id))
            .willReturn(studentStub)

        given(checkUserAuthority.execute(userStub.id))
            .willReturn(Authority.STUDENT)

        // when & then
        assertThrows<StudentInfoMismatchException> {
            resetStudentPasswordUseCase.execute(requestStub.copy(name = "이정윤아님", email = "이정윤아님@naver.com"))
        }
    }

    @Test
    fun `인증코드 존재하지 않음`() {
        // given
        given(queryUserPort.queryUserByAccountId(requestStub.accountId))
            .willReturn(userStub)

        given(queryStudentPort.queryStudentById(studentStub.id))
            .willReturn(studentStub)

        given(checkUserAuthority.execute(userStub.id))
            .willReturn(Authority.STUDENT)

        given(queryAuthCodePort.queryAuthCodeByEmail(userStub.email))
            .willReturn(null)

        // when & then
        assertThrows<AuthCodeNotFoundException> {
            resetStudentPasswordUseCase.execute(requestStub)
        }
    }

    @Test
    fun `인증코드 불일치`() {
        // given
        given(queryUserPort.queryUserByAccountId(requestStub.accountId))
            .willReturn(userStub)

        given(queryStudentPort.queryStudentById(studentStub.id))
            .willReturn(studentStub)

        given(checkUserAuthority.execute(userStub.id))
            .willReturn(Authority.STUDENT)

        given(queryAuthCodePort.queryAuthCodeByEmail(userStub.email))
            .willReturn(authCodeStub)

        // when & then
        assertThrows<AuthCodeMismatchException> {
            resetStudentPasswordUseCase.execute(requestStub.copy(authCode = "222222"))
        }
    }
}
