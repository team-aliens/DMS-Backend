package team.aliens.dms.domain.school.usecase

import org.junit.jupiter.api.Assertions.assertEquals
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
class CheckSchoolCodeUseCaseTest {

    @MockBean
    private lateinit var querySchoolPort: QuerySchoolPort

    private lateinit var checkSchoolCodeUseCase: CheckSchoolCodeUseCase

    private val schoolId = UUID.randomUUID()
    private val schoolCode = "AUTH1234"

    private val school by lazy {
        School(
            id = schoolId,
            name = "이정윤",
            code = schoolCode,
            question = "질문입니다",
            answer = "답변입니다",
            address = "주소입니다",
            contractStartedAt = LocalDate.now(),
            contractEndedAt = LocalDate.now()
        )
    }

    @BeforeEach
    fun setUp() {
        checkSchoolCodeUseCase = CheckSchoolCodeUseCase(querySchoolPort)
    }

    @Test
    fun `학교 인증코드 일치`() {
        //given
        given(querySchoolPort.querySchoolById(schoolId))
            .willReturn(school)

        // when
        val response = checkSchoolCodeUseCase.execute(schoolId, schoolCode)

        // then
        assertEquals(response, schoolId)
    }

    @Test
    fun `학교 존재하지 않음`() {
        // given
        given(querySchoolPort.querySchoolById(schoolId))
            .willReturn(null)

        // when & then
        assertThrows<SchoolNotFoundException> {
            checkSchoolCodeUseCase.execute(schoolId, schoolCode)
        }
    }

    @Test
    fun `학교 인증코드 불일치`() {
        // when & then
        assertThrows<SchoolNotFoundException> {
            checkSchoolCodeUseCase.execute(schoolId, "AUTH1234")
        }
    }
}