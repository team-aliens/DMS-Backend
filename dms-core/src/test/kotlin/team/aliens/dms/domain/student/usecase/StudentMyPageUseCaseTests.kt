package team.aliens.dms.domain.student.usecase

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.point.model.Phrase
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.StudentQueryPhrasePort
import team.aliens.dms.domain.point.stub.createPhraseStub
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.school.stub.createSchoolStub
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.student.spi.StudentQueryPointHistoryPort
import team.aliens.dms.domain.student.spi.StudentQuerySchoolPort
import team.aliens.dms.domain.student.spi.StudentSecurityPort
import team.aliens.dms.domain.student.stub.createStudentStub
import java.util.UUID

@ExtendWith(SpringExtension::class)
class StudentMyPageUseCaseTests {

    @MockBean
    private lateinit var securityPort: StudentSecurityPort

    @MockBean
    private lateinit var queryStudentPort: QueryStudentPort

    @MockBean
    private lateinit var querySchoolPort: StudentQuerySchoolPort

    @MockBean
    private lateinit var queryPointHistoryPort: StudentQueryPointHistoryPort

    @MockBean
    private lateinit var queryPhrasePort: StudentQueryPhrasePort

    private lateinit var studentMyPageUseCase: StudentMyPageUseCase

    @BeforeEach
    fun setUp() {
        studentMyPageUseCase = StudentMyPageUseCase(
            securityPort, queryStudentPort, querySchoolPort, queryPointHistoryPort, queryPhrasePort
        )
    }

    private val currentUserId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()

    private val studentStub by lazy {
        createStudentStub(
            id = currentUserId,
            schoolId = schoolId
        )
    }

    private val gcn = studentStub.gcn
    private val name = studentStub.name

    private val schoolStub by lazy {
        createSchoolStub(id = schoolId)
    }

    private val phraseStub by lazy {
        createPhraseStub()
    }

    @Test
    fun `학생 마이페이지 확인 성공`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryStudentPort.queryStudentByUserId(currentUserId))
            .willReturn(studentStub)

        given(querySchoolPort.querySchoolById(studentStub.schoolId))
            .willReturn(schoolStub)

        given(queryPointHistoryPort.queryBonusAndMinusTotalPointByStudentGcnAndName(gcn, name))
            .willReturn(Pair(1, 1))

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
    fun `문구 없는 경우`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryStudentPort.queryStudentByUserId(currentUserId))
            .willReturn(studentStub)

        given(queryStudentPort.queryStudentByUserId(currentUserId))
            .willReturn(studentStub)

        given(querySchoolPort.querySchoolById(studentStub.schoolId))
            .willReturn(schoolStub)

        given(queryPointHistoryPort.queryBonusAndMinusTotalPointByStudentGcnAndName(gcn, name))
            .willReturn(Pair(1, 1))

        given(queryPhrasePort.queryPhraseAllByPointTypeAndStandardPoint(type = PointType.BONUS, point = 1))
            .willReturn(listOf())

        given(queryPhrasePort.queryPhraseAllByPointTypeAndStandardPoint(type = PointType.MINUS, point = 1))
            .willReturn(listOf())

        // when
        val response = studentMyPageUseCase.execute()

        // then
        assertThat(response).isNotNull
        assertEquals(response.phrase, Phrase.NO_PHRASE)
    }

    @Test
    fun `학생 미존재`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryStudentPort.queryStudentByUserId(currentUserId))
            .willReturn(null)

        // when & then
        assertThrows<StudentNotFoundException> {
            studentMyPageUseCase.execute()
        }
    }

    @Test
    fun `학생 유저 미존재`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryStudentPort.queryStudentByUserId(currentUserId))
            .willReturn(studentStub)

        given(queryStudentPort.queryStudentByUserId(currentUserId))
            .willReturn(null)

        // when & then
        assertThrows<StudentNotFoundException> {
            studentMyPageUseCase.execute()
        }
    }

    @Test
    fun `학교 미존재`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryStudentPort.queryStudentByUserId(currentUserId))
            .willReturn(studentStub)

        given(queryStudentPort.queryStudentByUserId(currentUserId))
            .willReturn(studentStub)

        given(querySchoolPort.querySchoolById(studentStub.schoolId))
            .willReturn(null)

        // when & then
        assertThrows<SchoolNotFoundException> {
            studentMyPageUseCase.execute()
        }
    }
}
