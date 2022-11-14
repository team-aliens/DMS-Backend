package team.aliens.dms.domain.school.usecase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.school.dto.UpdateQuestionRequest
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.domain.school.spi.CommandSchoolPort
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import team.aliens.dms.domain.school.spi.SchoolQueryUserPort
import team.aliens.dms.domain.school.spi.SchoolSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@ExtendWith(SpringExtension::class)
class UpdateQuestionUseCaseTests {

    @MockBean
    private lateinit var querySchoolPort: QuerySchoolPort

    @MockBean
    private lateinit var commandSchoolPort: CommandSchoolPort

    @MockBean
    private lateinit var securityPort: SchoolSecurityPort

    @MockBean
    private lateinit var queryUserPort: SchoolQueryUserPort

    private lateinit var updateQuestionUseCase: UpdateQuestionUseCase

    @BeforeEach
    fun setUp() {
        updateQuestionUseCase = UpdateQuestionUseCase(
            querySchoolPort, commandSchoolPort, securityPort, queryUserPort
        )
    }

    private val currentUserId = UUID.randomUUID()

    private val requestStub by lazy {
        UpdateQuestionRequest(
            question = "new question",
            answer = "new answer"
        )
    }

    private val userStub by lazy {
        User(
            id = currentUserId,
            schoolId = schoolStub.id,
            accountId = "계정아이디",
            password = "test password",
            email = "이메일",
            name = "이름",
            profileImageUrl = "https://~",
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )
    }

    private val schoolStub by lazy {
        School(
            id = UUID.randomUUID(),
            name = "test name",
            code = "test code",
            question = "test question",
            answer = "test answer",
            address = "test address",
            contractStartedAt = LocalDate.now(),
            contractEndedAt = LocalDate.now(),
        )
    }

    @Test
    fun `질문 변경 성공`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(userStub)

        given(querySchoolPort.querySchoolById(schoolStub.id))
            .willReturn(schoolStub)

        // when & then
        assertDoesNotThrow {
            updateQuestionUseCase.execute(requestStub)
        }
    }

    @Test
    fun `유저를 찾을 수 없음`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(null)

        // when & then
        assertThrows<UserNotFoundException> {
            updateQuestionUseCase.execute(requestStub)
        }
    }

    @Test
    fun `학교를 찾을 수 없음`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(userStub)

        given(querySchoolPort.querySchoolById(schoolStub.id))
            .willReturn(null)

        // when & then
        assertThrows<SchoolNotFoundException> {
            updateQuestionUseCase.execute(requestStub)
        }
    }
}