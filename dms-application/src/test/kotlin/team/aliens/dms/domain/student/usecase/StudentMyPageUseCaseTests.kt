package team.aliens.dms.domain.student.usecase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.student.spi.StudentQueryPointPort
import team.aliens.dms.domain.student.spi.StudentQuerySchoolPort
import team.aliens.dms.domain.student.spi.StudentSecurityPort
import java.time.LocalDate
import java.util.UUID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.assertThrows
import team.aliens.dms.domain.point.model.Phrase
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.StudentQueryPhrasePort
import team.aliens.dms.domain.student.model.Sex

@ExtendWith(SpringExtension::class)
class StudentMyPageUseCaseTests {

    @MockBean
    private lateinit var securityPort: StudentSecurityPort

    @MockBean
    private lateinit var queryStudentPort: QueryStudentPort

    @MockBean
    private lateinit var querySchoolPort: StudentQuerySchoolPort

    @MockBean
    private lateinit var queryPointPort: StudentQueryPointPort

    @MockBean
    private lateinit var queryPhrasePort: StudentQueryPhrasePort

    private lateinit var studentMyPageUseCase: StudentMyPageUseCase

    @BeforeEach
    fun setUp() {
        studentMyPageUseCase = StudentMyPageUseCase(
            securityPort, queryStudentPort, querySchoolPort, queryPointPort, queryPhrasePort
        )
    }

    private val currentUserId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()

    private val studentStub by lazy {
        Student(
            id = currentUserId,
            roomId = UUID.randomUUID(),
            roomNumber = 123,
            schoolId = schoolId,
            grade = 1,
            classRoom = 1,
            number = 1,
            name = "??????",
            profileImageUrl = "https://~",
            sex = Sex.FEMALE
        )
    }

    private val schoolStub by lazy {
        School(
            id = schoolId,
            name = "????????????",
            code = "12345678",
            question = "??????",
            answer = "??????",
            address = "??????",
            contractStartedAt = LocalDate.now(),
            contractEndedAt = null
        )
    }

    private val phraseStub by lazy {
        Phrase(
            id = UUID.randomUUID(),
            content = "?????? ?????? ??????",
            type = PointType.BONUS,
            standard = 0
        )
    }

    @Test
    fun `?????? ??????????????? ?????? ??????`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryStudentPort.queryStudentById(currentUserId))
            .willReturn(studentStub)

        given(queryStudentPort.queryStudentById(currentUserId))
            .willReturn(studentStub)

        given(querySchoolPort.querySchoolById(studentStub.schoolId))
            .willReturn(schoolStub)

        given(queryPointPort.queryTotalBonusPoint(studentStub.id))
            .willReturn(1)

        given(queryPointPort.queryTotalMinusPoint(studentStub.id))
            .willReturn(1)

        given(queryPhrasePort.queryPhraseAllByPointTypeAndStandardPoint(type = PointType.BONUS, point = 1))
            .willReturn(listOf(phraseStub))

        given(queryPhrasePort.queryPhraseAllByPointTypeAndStandardPoint(type = PointType.MINUS, point = 1))
            .willReturn(listOf(phraseStub))

        // when
        val response = studentMyPageUseCase.execute()

        // then
        assertThat(response).isNotNull
    }

    @Test
    fun `?????? ?????????`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryStudentPort.queryStudentById(currentUserId))
            .willReturn(null)

        // when & then
        assertThrows<StudentNotFoundException> {
            studentMyPageUseCase.execute()
        }
    }

    @Test
    fun `?????? ?????? ?????????`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryStudentPort.queryStudentById(currentUserId))
            .willReturn(studentStub)

        given(queryStudentPort.queryStudentById(currentUserId))
            .willReturn(null)

        // when & then
        assertThrows<StudentNotFoundException> {
            studentMyPageUseCase.execute()
        }
    }

    @Test
    fun `?????? ?????????`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryStudentPort.queryStudentById(currentUserId))
            .willReturn(studentStub)

        given(queryStudentPort.queryStudentById(currentUserId))
            .willReturn(studentStub)

        given(querySchoolPort.querySchoolById(studentStub.schoolId))
            .willReturn(null)

        // when & then
        assertThrows<SchoolNotFoundException> {
            studentMyPageUseCase.execute()
        }
    }
}