package team.aliens.dms.domain.student.usecase

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.dto.TokenResponse
import team.aliens.dms.domain.auth.exception.AuthCodeMismatchException
import team.aliens.dms.domain.auth.exception.AuthCodeNotFoundException
import team.aliens.dms.domain.auth.model.AuthCode
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.auth.model.EmailType
import team.aliens.dms.domain.school.exception.AnswerMismatchException
import team.aliens.dms.domain.school.exception.SchoolCodeMismatchException
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.domain.student.dto.SignUpRequest
import team.aliens.dms.domain.student.dto.SignUpResponse
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.spi.CommandStudentPort
import team.aliens.dms.domain.student.spi.StudentCommandUserPort
import team.aliens.dms.domain.student.spi.StudentJwtPort
import team.aliens.dms.domain.student.spi.StudentQueryAuthCodePort
import team.aliens.dms.domain.student.spi.StudentQuerySchoolPort
import team.aliens.dms.domain.student.spi.StudentQueryUserPort
import team.aliens.dms.domain.student.spi.StudentSecurityPort
import team.aliens.dms.domain.user.exception.UserAccountIdExistsException
import team.aliens.dms.domain.user.exception.UserEmailExistsException
import team.aliens.dms.domain.user.model.User
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@ExtendWith(SpringExtension::class)
class SignUpUseCaseTests {

    @MockBean
    private lateinit var commandStudentPort: CommandStudentPort

    @MockBean
    private lateinit var commandUserPort: StudentCommandUserPort

    @MockBean
    private lateinit var querySchoolPort: StudentQuerySchoolPort

    @MockBean
    private lateinit var queryAuthCodePort: StudentQueryAuthCodePort

    @MockBean
    private lateinit var queryUserPort: StudentQueryUserPort

    @MockBean
    private lateinit var securityPort: StudentSecurityPort

    @MockBean
    private lateinit var jwtPort: StudentJwtPort

    private lateinit var signUpUseCase: SignUpUseCase

    @BeforeEach
    fun setUp() {
        signUpUseCase = SignUpUseCase(
            commandStudentPort,
            commandUserPort,
            querySchoolPort,
            queryUserPort,
            queryAuthCodePort,
            securityPort,
            jwtPort
        )
    }

    private val id = UUID.randomUUID()
    private val code = "12345678"
    private val email = "test@test.com"
    private val accountId = "test accountId"
    private val question = "test question"
    private val answer = "test answer"
    private val name = "test name"
    private val profileImageUrl = "test profileImage"

    private val schoolStub by lazy {
        School(
            id = id,
            name = "test name",
            code = code,
            question = question,
            answer = answer,
            address = "주소",
            contractStartedAt = LocalDate.now(),
            contractEndedAt = null
        )
    }

    private val authCodeStub by lazy {
        AuthCode(
            code = "123412",
            email = email,
            type = EmailType.SIGNUP
        )
    }

    private val userStub by lazy {
        User(
            schoolId = schoolStub.id,
            accountId = accountId,
            password = "encoded password",
            email = email,
            authority = Authority.STUDENT,
            createdAt = null,
            deletedAt = null
        )
    }

    private val savedUserStub by lazy {
        User(
            id = id,
            schoolId = schoolStub.id,
            accountId = accountId,
            password = "encoded password",
            email = email,
            authority = Authority.STUDENT,
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )
    }

    private val studentStub by lazy {
        Student(
            id = savedUserStub.id,
            roomId = UUID.randomUUID(),
            roomNumber = 123,
            schoolId = schoolStub.id,
            grade = 1,
            classRoom = 1,
            number = 1,
            name = name,
            profileImageUrl = profileImageUrl
        )
    }

    private val savedStudentStub by lazy {
        Student(
            id = savedUserStub.id,
            roomId = UUID.randomUUID(),
            roomNumber = 123,
            schoolId = schoolStub.id,
            grade = 1,
            classRoom = 1,
            number = 1,
            name = name,
            profileImageUrl = profileImageUrl
        )
    }

    private val requestStub by lazy {
        SignUpRequest(
            schoolCode = code,
            schoolAnswer = answer,
            email = email,
            authCode = "123412",
            grade = 1,
            classRoom = 1,
            number = 17,
            accountId = accountId,
            password = "test password",
            profileImageUrl = profileImageUrl
        )
    }

    private val tokenResponseStub by lazy {
        TokenResponse(
            accessToken = "test access token",
            accessTokenExpiredAt = LocalDateTime.now(),
            refreshToken = "test refresh token",
            refreshTokenExpiredAt = LocalDateTime.now()
        )
    }

    private val signUpResponseStub by lazy {
        SignUpResponse(
            accessToken = tokenResponseStub.accessToken,
            accessTokenExpiredAt = tokenResponseStub.accessTokenExpiredAt,
            refreshToken = tokenResponseStub.refreshToken,
            refreshTokenExpiredAt = tokenResponseStub.refreshTokenExpiredAt,
            features = SignUpResponse.Features(
                mealService = true,
                noticeService = true,
                pointService = true,
            )
        )
    }

    @Test
    fun `회원가입 성공`() {
        // given
        given(querySchoolPort.querySchoolByCode(code))
            .willReturn(schoolStub)

        given(queryUserPort.existsUserByEmail(email))
            .willReturn(false)

        given(queryAuthCodePort.queryAuthCodeByEmail(email))
            .willReturn(authCodeStub)

        given(queryUserPort.existsUserByAccountId(accountId))
            .willReturn(false)

        given(securityPort.encodePassword(requestStub.password))
            .willReturn(userStub.password)

        given(commandUserPort.saveUser(userStub))
            .willReturn(savedUserStub)

        given(commandStudentPort.saveStudent(studentStub))
            .willReturn(savedStudentStub)

        given(jwtPort.receiveToken(id, Authority.STUDENT))
            .willReturn(tokenResponseStub)

        // when
        val response = signUpUseCase.execute(requestStub)

        // then
        assertThat(response).isEqualTo(signUpResponseStub)
    }

    @Test
    fun `학교 인증코드에 해당하는 학교가 존재하지 않음`() {
        // given
        given(querySchoolPort.querySchoolByCode(code))
            .willReturn(null)

        // when & then
        assertThrows<SchoolCodeMismatchException> {
            signUpUseCase.execute(requestStub)
        }
    }

    @Test
    fun `학교 확인 질문에 대한 답변이 일치하지 않음`() {
        val wrongAnswerSchool = schoolStub.copy(answer = "wrong answer")

        // given
        given(querySchoolPort.querySchoolByCode(code))
            .willReturn(wrongAnswerSchool)

        // when & then
        assertThrows<AnswerMismatchException> {
            signUpUseCase.execute(requestStub)
        }
    }

    @Test
    fun `이메일이 이미 존재함`() {
        // given
        given(querySchoolPort.querySchoolByCode(code))
            .willReturn(schoolStub)

        given(queryUserPort.existsUserByEmail(email))
            .willReturn(true)

        // when & then
        assertThrows<UserEmailExistsException> {
            signUpUseCase.execute(requestStub)
        }
    }

    @Test
    fun `이메일 인증코드가 존재하지 않음`() {
        // given
        given(querySchoolPort.querySchoolByCode(code))
            .willReturn(schoolStub)

        given(queryUserPort.existsUserByEmail(email))
            .willReturn(false)

        given(queryAuthCodePort.queryAuthCodeByEmail(email))
            .willReturn(null)

        // when & then
        assertThrows<AuthCodeNotFoundException> {
            signUpUseCase.execute(requestStub)
        }
    }

    @Test
    fun `이메일 인증코드가 일치하지 않음`() {
        val wrongCodeAuthCode = authCodeStub.copy(code = "wrong code")

        // given
        given(querySchoolPort.querySchoolByCode(code))
            .willReturn(schoolStub)

        given(queryUserPort.existsUserByEmail(email))
            .willReturn(false)

        given(queryAuthCodePort.queryAuthCodeByEmail(email))
            .willReturn(wrongCodeAuthCode)

        // when & then
        assertThrows<AuthCodeMismatchException> {
            signUpUseCase.execute(requestStub)
        }
    }

    @Test
    fun `아이디가 이미 존재함`() {
        // given
        given(querySchoolPort.querySchoolByCode(code))
            .willReturn(schoolStub)

        given(queryUserPort.existsUserByEmail(email))
            .willReturn(false)

        given(queryAuthCodePort.queryAuthCodeByEmail(email))
            .willReturn(authCodeStub)

        given(queryUserPort.existsUserByAccountId(accountId))
            .willReturn(true)

        // when & then
        assertThrows<UserAccountIdExistsException> {
            signUpUseCase.execute(requestStub)
        }
    }
}