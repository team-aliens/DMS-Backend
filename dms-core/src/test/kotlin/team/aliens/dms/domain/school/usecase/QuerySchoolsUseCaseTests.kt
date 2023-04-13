package team.aliens.dms.domain.school.usecase

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import team.aliens.dms.domain.school.stub.createSchoolStub

@ExtendWith(SpringExtension::class)
class QuerySchoolsUseCaseTests {

    @MockBean
    private lateinit var querySchoolPort: QuerySchoolPort

    private lateinit var querySchoolsUseCase: QuerySchoolsUseCase

    @BeforeEach
    fun setUp() {
        querySchoolsUseCase = QuerySchoolsUseCase(querySchoolPort)
    }

    private val schoolStub by lazy {
        createSchoolStub()
    }

    private val schoolStub2 by lazy {
        createSchoolStub()
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
