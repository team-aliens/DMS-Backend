package team.aliens.dms.domain.student.usecase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.dto.TokenResponse
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.domain.student.dto.SignupRequest
import team.aliens.dms.domain.student.dto.TokenAndFeaturesResponse
import team.aliens.dms.domain.student.spi.*
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

    private val code = "QWER!@#$"

    private val email = "test@tes.com"

    private val accountId = "accountId"

    private val answer = "정답"

    private val school by lazy {
        School(
            id = id,
            name = "name",
            code = code,
            question = "확인 질문",
            answer = ,
            address = "주소",
            contractStartedAt = LocalDate.now(),
            contractEndedAt = null
        )
    }

    private val tokenResponse by lazy {
        TokenResponse(
            accessToken = "sldagkmlgmldkg",
            expiredAt = LocalDateTime.now(),
            refreshToken = "dlkgmsalgmslgmalkmlkam"
        )
    }

    @Test
    fun `회원가입 성공`() {
        val tokenAndFeaturesResponse = TokenAndFeaturesResponse(
            accessToken = tokenResponse.accessToken,
            expiredAt = tokenResponse.expiredAt,
            refreshToken = tokenResponse.refreshToken,
            features = TokenAndFeaturesResponse.Features(
                mealService = true,
                noticeService = true,
                pointService = true,
            )
        )
        val request = SignupRequest(
            schoolCode = code,
            schoolAnswer = answer,
            email = email,
            authCode = "123412",
            grade = 1,
            classRoom = 1,
            number = 17,
            accountId = accountId,
            password = "!@$!@SSDG",
            profileImageUrl = null
        )

        given(querySchoolPort.querySchoolByCode(code))
            .willReturn(school)
        given(queryUserPort.existsByEmail(email))
            .willReturn(false)
        given(queryUserPort.existsByAccountId(accountId))
            .willReturn(false)
        given(jwtPort.receiveToken(id, Authority.STUDENT))
            .willReturn(tokenResponse)

        val response = singUpUseCase.execute(request)

        assertEquals()
    }

}