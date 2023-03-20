package team.aliens.dms.domain.school.usecase

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.school.exception.SchoolCodeMismatchException
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import team.aliens.dms.domain.school.stub.createSchoolStub
import java.util.UUID

@ExtendWith(SpringExtension::class)
class CheckSchoolCodeUseCaseTests {

    @MockBean
    private lateinit var querySchoolPort: QuerySchoolPort

    private lateinit var checkSchoolCodeUseCase: CheckSchoolCodeUseCase

    @BeforeEach
    fun setUp() {
        checkSchoolCodeUseCase = CheckSchoolCodeUseCase(querySchoolPort)
    }

    private val schoolId = UUID.randomUUID()
    private val schoolCode = "AUTH1234"

    private val schoolStub by lazy {
        createSchoolStub(
            id = schoolId,
            code = schoolCode
        )
    }

    @Test
    fun `학교 인증코드 일치`() {
        // given
        given(querySchoolPort.querySchoolByCode(schoolCode))
            .willReturn(schoolStub)

        // when
        val response = checkSchoolCodeUseCase.execute(schoolCode)

        // then
        assertEquals(response, schoolId)
    }

    @Test
    fun `학교 인증코드 불일치`() {
        // given
        given(querySchoolPort.querySchoolByCode(schoolCode))
            .willReturn(null)

        // when & then
        assertThrows<SchoolCodeMismatchException> {
            checkSchoolCodeUseCase.execute(schoolCode)
        }
    }
}
