package team.aliens.dms.domain.school.usecase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.school.dto.UpdateQuestionRequest
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.school.spi.CommandSchoolPort
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import team.aliens.dms.domain.school.spi.SchoolQueryUserPort
import team.aliens.dms.domain.school.spi.SchoolSecurityPort
import team.aliens.dms.domain.school.stub.createSchoolStub
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.stub.createUserStub
import java.util.UUID

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
        createUserStub(
            id = currentUserId,
            schoolId = schoolStub.id,
            authority = Authority.MANAGER
        )
    }

    private val schoolStub by lazy {
        createSchoolStub()
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
