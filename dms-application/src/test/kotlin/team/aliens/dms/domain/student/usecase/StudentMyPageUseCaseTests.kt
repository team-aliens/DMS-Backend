package team.aliens.dms.domain.student.usecase

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
import team.aliens.dms.domain.student.dto.StudentMyPageResponse
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.student.spi.StudentQueryPointPort
import team.aliens.dms.domain.student.spi.StudentQuerySchoolPort
import team.aliens.dms.domain.student.spi.StudentSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import java.time.LocalDate
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
    private lateinit var queryPointPort: StudentQueryPointPort

    private lateinit var studentMyPageUseCase: StudentMyPageUseCase

    @BeforeEach
    fun setUp() {
        studentMyPageUseCase = StudentMyPageUseCase(
            securityPort, queryStudentPort, querySchoolPort, queryPointPort
        )
    }

    private val currentUserId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()

    private val studentStub by lazy {
        Student(
            studentId = currentUserId,
            roomId = UUID.randomUUID(),
            schoolId = schoolId,
            grade = 1,
            classRoom = 1,
            number = 1,
            name = "이름",
            profileImageUrl = "https://~"
        )
    }

    private val studentProfileNullStub by lazy {
        Student(
            studentId = currentUserId,
            roomId = UUID.randomUUID(),
            schoolId = schoolId,
            grade = 1,
            classRoom = 1,
            number = 1,
            name = "이름",
            profileImageUrl = "https://~"
        )
    }

    private val schoolStub by lazy {
        School(
            id = schoolId,
            name = "학교이름",
            code = "12345678",
            question = "질문",
            answer = "정답",
            address = "주소",
            contractStartedAt = LocalDate.now(),
            contractEndedAt = null
        )
    }

    private val studentMyPageResponseStub by lazy {
        StudentMyPageResponse(
            schoolName = "학교이름",
            name = "이름",
            gcn = "2310",
            profileImageUrl = "https://~",
            bonusPoint = 1,
            minusPoint = 1,
            phrase = "잘하자"
        )
    }

    private val studentMyPageProfileUrlNullResponseStub by lazy {
        StudentMyPageResponse(
            schoolName = "학교이름",
            name = "이름",
            gcn = "2310",
            profileImageUrl = Student.PROFILE_IMAGE,
            bonusPoint = 1,
            minusPoint = 1,
            phrase = "잘하자"
        )
    }

    @Test
    fun `학생 마이페이지 확인 성공`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryStudentPort.queryStudentById(currentUserId))
            .willReturn(studentStub)

        given(queryStudentPort.queryStudentById(currentUserId))
            .willReturn(studentStub)

        given(querySchoolPort.querySchoolById(studentStub.schoolId))
            .willReturn(schoolStub)

        given(queryPointPort.queryTotalBonusPoint(studentStub.studentId))
            .willReturn(1)

        given(queryPointPort.queryTotalMinusPoint(studentStub.studentId))
            .willReturn(1)

        // when
        val response = studentMyPageUseCase.execute()

        // then
        assertEquals(response, studentMyPageResponseStub)
    }

    @Test
    fun `학생 마이페이지 확인 성공 프로필 기본이미지`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryStudentPort.queryStudentById(currentUserId))
            .willReturn(studentStub)

        given(queryStudentPort.queryStudentById(currentUserId))
            .willReturn(studentProfileNullStub)

        given(querySchoolPort.querySchoolById(studentStub.schoolId))
            .willReturn(schoolStub)

        given(queryPointPort.queryTotalBonusPoint(studentStub.studentId))
            .willReturn(1)

        given(queryPointPort.queryTotalMinusPoint(studentStub.studentId))
            .willReturn(1)

        // when
        val response = studentMyPageUseCase.execute()

        // then
        assertEquals(response, studentMyPageProfileUrlNullResponseStub)
    }

    @Test
    fun `학생 미존재`() {
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
    fun `학생 유저 미존재`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryStudentPort.queryStudentById(currentUserId))
            .willReturn(studentStub)

        given(queryStudentPort.queryStudentById(currentUserId))
            .willReturn(null)

        // when & then
        assertThrows<UserNotFoundException> {
            studentMyPageUseCase.execute()
        }
    }

    @Test
    fun `학교 미존재`() {
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