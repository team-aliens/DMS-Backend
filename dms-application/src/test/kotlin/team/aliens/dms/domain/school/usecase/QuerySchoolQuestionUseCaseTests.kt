package team.aliens.dms.domain.school.usecase

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import java.time.LocalDate
import java.util.UUID

@ExtendWith(SpringExtension::class)
class QuerySchoolQuestionUseCaseTests {

    @MockBean
    private lateinit var querySchoolPort: QuerySchoolPort

    private lateinit var querySchoolQuestionUseCase: QuerySchoolQuestionUseCase

    @BeforeEach
    fun setUp() {
        querySchoolQuestionUseCase = QuerySchoolQuestionUseCase(querySchoolPort)
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
    fun `학교 질문 조회 성공`() {
        val schoolId = UUID.randomUUID()

        // given
        given(querySchoolPort.querySchoolById(schoolId))
            .willReturn(schoolStub)

        // when
        val response = querySchoolQuestionUseCase.execute(schoolId)

        // then
        assertThat(response).isEqualTo("test question")
    }

    @Test
    fun `학교가 존재하지 않음`() {
        val schoolId = UUID.randomUUID()

        // given
        given(querySchoolPort.querySchoolById(schoolId))
            .willReturn(null)

        // when & then
        assertThrows<SchoolNotFoundException> {
            querySchoolQuestionUseCase.execute(schoolId)
        }
    }
}