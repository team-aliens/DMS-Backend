package team.aliens.dms.domain.school.usecase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.school.exception.AnswerMismatchException
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import java.time.LocalDate
import java.util.UUID

@ExtendWith(SpringExtension::class)
class CheckSchoolAnswerUseCaseTests {

    @MockBean
    private lateinit var querySchoolPort: QuerySchoolPort

    private lateinit var checkSchoolAnswerUseCase: CheckSchoolAnswerUseCase

    @BeforeEach
    fun setUp() {
        checkSchoolAnswerUseCase = CheckSchoolAnswerUseCase(querySchoolPort)
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
    fun `답변이 일치함`() {
        val schoolId = UUID.randomUUID()

        // given
        given(querySchoolPort.querySchoolById(schoolId))
            .willReturn(schoolStub)

        // when & then
        assertDoesNotThrow {
            checkSchoolAnswerUseCase.execute(schoolId, schoolStub.answer)
        }
    }

    @Test
    fun `학교가 존재하지 않음`() {
        val schoolId = UUID.randomUUID()

        // given
        given(querySchoolPort.querySchoolById(schoolId))
            .willReturn(null)

        // when & then
        assertThrows<SchoolNotFoundException> {
            checkSchoolAnswerUseCase.execute(schoolId, "test answer")
        }
    }

    @Test
    fun `답변이 일치하지 않음`() {
        val schoolId = UUID.randomUUID()

        // given
        given(querySchoolPort.querySchoolById(schoolId))
            .willReturn(schoolStub)

        // when & then
        assertThrows<AnswerMismatchException> {
            checkSchoolAnswerUseCase.execute(schoolId, "wrong answer")
        }
    }
}
