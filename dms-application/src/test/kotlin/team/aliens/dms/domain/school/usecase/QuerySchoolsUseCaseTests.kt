package team.aliens.dms.domain.school.usecase

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import java.time.LocalDate
import java.util.UUID

@ExtendWith(SpringExtension::class)
class QuerySchoolsUseCaseTests {

    @MockBean
    private lateinit var querySchoolPort: QuerySchoolPort

    private lateinit var querySchoolsUseCase: QuerySchoolsUseCase

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

    private val schoolStub2 by lazy {
        School(
            id = UUID.randomUUID(),
            name = "test name2",
            code = "test code2",
            question = "test question2",
            answer = "test answer2",
            address = "test address2",
            contractStartedAt = LocalDate.now(),
            contractEndedAt = LocalDate.now(),
        )
    }

    @BeforeEach
    fun setUp() {
        querySchoolsUseCase = QuerySchoolsUseCase(querySchoolPort)
    }

    @Test
    fun `학교 목록 조회 성공`() {
        // given
        given(querySchoolPort.queryAllSchools())
            .willReturn(
                listOf(schoolStub, schoolStub2)
            )

        // when
        val response = querySchoolsUseCase.execute()

        // then
        assertThat(response).isNotNull
    }

}