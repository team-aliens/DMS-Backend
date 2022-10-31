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
import team.aliens.dms.domain.student.dto.CheckStudentGcnRequest
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.student.spi.StudentQuerySchoolPort
import team.aliens.dms.domain.student.spi.StudentQueryUserPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@ExtendWith(SpringExtension::class)
class CheckStudentGcnUseCaseTests {

    @MockBean
    private lateinit var querySchoolPort: StudentQuerySchoolPort

    @MockBean
    private lateinit var queryStudentPort: QueryStudentPort

    @MockBean
    private lateinit var queryUserPort: StudentQueryUserPort

    private lateinit var checkStudentGcnUseCase: CheckStudentGcnUseCase

    @BeforeEach
    fun setUp() {
        checkStudentGcnUseCase = CheckStudentGcnUseCase(
            querySchoolPort, queryStudentPort, queryUserPort
        )
    }

    private val requestStub by lazy {
        CheckStudentGcnRequest(
            schoolId = UUID.randomUUID(),
            grade = 1,
            classRoom = 2,
            number = 3
        )
    }

    private val schoolStub by lazy {
        School(
            id = UUID.randomUUID(),
            name = "대마고",
            code = "코드",
            question = "질문",
            answer = "답변",
            address = "주소",
            contractStartedAt = LocalDate.now(),
            contractEndedAt = null
        )
    }

    private val studentStub by lazy {
        Student(
            studentId = UUID.randomUUID(),
            roomNumber = 1,
            schoolId = schoolStub.id,
            grade = 1,
            classRoom = 2,
            number = 3
        )
    }

    private val userStub by lazy {
        User(
            id = studentStub.studentId,
            schoolId = schoolStub.id,
            accountId = "아이디",
            password = "비번",
            email = "이메일",
            name = "이름",
            profileImageUrl = "https://~",
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )
    }

    @Test
    fun `학번 확인 성공`() {
        // given
        given(querySchoolPort.querySchoolById(requestStub.schoolId))
            .willReturn(schoolStub)

        given(queryStudentPort.queryStudentBySchoolIdAndGcn(schoolStub.id, requestStub.grade, requestStub.classRoom, requestStub.number))
            .willReturn(studentStub)

        given(queryUserPort.queryUserById(studentStub.studentId))
            .willReturn(userStub)

        // when
        val response = checkStudentGcnUseCase.execute(requestStub)

        // then
        assertEquals(response, userStub.name)
    }


    @Test
    fun `학교 존재하지 않음`() {
        // given
        given(querySchoolPort.querySchoolById(requestStub.schoolId))
            .willReturn(null)

        // when & then
        assertThrows<SchoolNotFoundException> {
            checkStudentGcnUseCase.execute(requestStub)
        }
    }

    @Test
    fun `학생 존재하지 않음`() {
        // given
        given(querySchoolPort.querySchoolById(requestStub.schoolId))
            .willReturn(schoolStub)

        given(queryStudentPort.queryStudentBySchoolIdAndGcn(schoolStub.id, requestStub.grade, requestStub.classRoom, requestStub.number))
            .willReturn(null)

        // when & then
        assertThrows<StudentNotFoundException> {
            checkStudentGcnUseCase.execute(requestStub)
        }
    }

    @Test
    fun `사용자 존재하지 않음`() {
        // given
        given(querySchoolPort.querySchoolById(requestStub.schoolId))
            .willReturn(schoolStub)

        given(queryStudentPort.queryStudentBySchoolIdAndGcn(schoolStub.id, requestStub.grade, requestStub.classRoom, requestStub.number))
            .willReturn(studentStub)

        given(queryUserPort.queryUserById(studentStub.studentId))
            .willReturn(null)

        // when & then
        assertThrows<UserNotFoundException> {
            checkStudentGcnUseCase.execute(requestStub)
        }
    }
}