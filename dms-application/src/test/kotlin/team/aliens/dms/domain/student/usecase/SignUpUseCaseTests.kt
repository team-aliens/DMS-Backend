package team.aliens.dms.domain.student.usecase

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.dto.TokenResponse
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.domain.student.dto.SignupRequest
import team.aliens.dms.domain.student.dto.TokenAndFeaturesResponse
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.spi.*
import team.aliens.dms.domain.user.model.User
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@ExtendWith(SpringExtension::class)
class SignUpUseCaseTests {

    @MockBean
    private lateinit var commandStudentPort: CommandStudentPort

    @MockBean
    private lateinit var commandUserPort: StudentCommandUserPort

    @MockBean
    private lateinit var querySchoolPort: StudentQuerySchoolPort

    @MockBean
    private lateinit var queryUserPort: StudentQueryUserPort

    @MockBean
    private lateinit var securityPort: StudentSecurityPort

    @MockBean
    private lateinit var jwtPort: StudentJwtPort

    private lateinit var singUpUseCase: SignupUseCase

    @BeforeEach
    fun setUp() {
        singUpUseCase = SignupUseCase(
            commandStudentPort,
            commandUserPort,
            querySchoolPort,
            queryUserPort,
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

    private val userStub by lazy {
        User(
            schoolId = schoolStub.id,
            accountId = accountId,
            password = "encoded password",
            email = email,
            name = name,
            profileImageUrl = profileImageUrl,
            createdAt = null,
            deletedAt = null
        )
    }

    private val savedUserStub: User by lazy {
        User(
            id = UUID.randomUUID(),
            schoolId = schoolStub.id,
            accountId = accountId,
            password = "encoded password",
            email = email,
            name = name,
            profileImageUrl = profileImageUrl,
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )
    }

    private val studentStub by lazy {
        Student(
            studentId = savedUserStub.id,
            roomNumber = 318,
            schoolId = schoolStub.id,
            grade = 1,
            classRoom = 1,
            number = 1
        )
    }

    private val savedStudentStub by lazy {
        Student(
            studentId = savedUserStub.id,
            roomNumber = 318,
            schoolId = schoolStub.id,
            grade = 1,
            classRoom = 1,
            number = 1
        )
    }

    private val requestStub by lazy {
        SignupRequest(
            schoolCode = code,
            schoolAnswer = answer,
            email = email,
            authCode = "123412",
            grade = 1,
            classRoom = 1,
            number = 17,
            accountId = accountId,
            password = "test password",
            profileImageUrl = null
        )
    }

    private val tokenResponseStub by lazy {
        TokenResponse(
            accessToken = "test access token",
            expiredAt = LocalDateTime.now(),
            refreshToken = "test refresh token"
        )
    }

    private val tokenAndFeaturesResponseStub by lazy {
        TokenAndFeaturesResponse(
            accessToken = tokenResponseStub.accessToken,
            expiredAt = tokenResponseStub.expiredAt,
            refreshToken = tokenResponseStub.refreshToken,
            features = TokenAndFeaturesResponse.Features(
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

        given(queryUserPort.existsByEmail(email))
            .willReturn(false)

        given(queryUserPort.existsByAccountId(accountId))
            .willReturn(false)

        given(securityPort.encode(requestStub.password))
            .willReturn(userStub.password)

        given(commandUserPort.saveUser(userStub))
            .willReturn(savedUserStub)

        given(commandStudentPort.saveStudent(studentStub))
            .willReturn(savedStudentStub)

        given(jwtPort.receiveToken(id, Authority.STUDENT))
            .willReturn(tokenResponseStub)

        // when
        val response = singUpUseCase.execute(requestStub)

        // then
        assertDoesNotThrow {
            singUpUseCase.execute(requestStub)
        }
        assertThat(response).isEqualTo(tokenAndFeaturesResponseStub)
    }

}