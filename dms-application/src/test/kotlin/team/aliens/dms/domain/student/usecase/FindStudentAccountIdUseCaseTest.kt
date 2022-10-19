package team.aliens.dms.domain.student.usecase

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.student.dto.FindAccountIdRequest
import team.aliens.dms.domain.student.exception.StudentInfoNotMatchedException
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.student.spi.StudentQueryUserPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import team.aliens.dms.global.spi.CoveredEmailPort
import java.time.LocalDateTime
import java.util.UUID

@ExtendWith(SpringExtension::class)
class FindStudentAccountIdUseCaseTest {

    @MockBean
    private lateinit var queryStudentPort: QueryStudentPort

    @MockBean
    private lateinit var queryUserPort: StudentQueryUserPort

    @MockBean
    private lateinit var coveredEmailPort: CoveredEmailPort

    private lateinit var findStudentAccountIdUseCase: FindStudentAccountIdUseCase

    private val schoolId = UUID.randomUUID()


    private val request by lazy {
        FindAccountIdRequest(
            name = "이정윤",
            grade = 2,
            classRoom = 3,
            number = 10
        )
    }

    private val student by lazy {
        Student(
            studentId = UUID.randomUUID(),
            roomNumber = 318,
            schoolId = schoolId,
            grade = request.grade,
            classRoom = request.classRoom,
            number = request.number
        )
    }

    private val user by lazy {
        User(
            id = UUID.randomUUID(),
            schoolId = schoolId,
            accountId = "이정윤123",
            password = "이정윤123!",
            email = "이정윤14@naver.com",
            name = "이정윤",
            profileImageUrl = "http~",
            createdAt = LocalDateTime.now(),
            deletedAt = LocalDateTime.now()
        )
    }

    @BeforeEach
    fun setUp() {
        findStudentAccountIdUseCase = FindStudentAccountIdUseCase(queryStudentPort, queryUserPort, coveredEmailPort)
    }

    @Test
    fun `아이디 찾기 성공`() {
        // given
        given(queryStudentPort.queryStudentBySchoolIdAndGcn(schoolId, request.grade, request.classRoom, request.number))
            .willReturn(student)

        given(queryUserPort.queryByUserId(student.studentId))
            .willReturn(user)

        given(!queryStudentPort.existsByGcn(request.grade, request.classRoom, request.number))
            .willReturn(true)

        given(coveredEmailPort.coveredEmail(user.email))
            .willReturn("dlw********")

        // when
        val response = findStudentAccountIdUseCase.execute(schoolId, request)

        // then
        assertEquals(response, "dlw********")
    }

    @Test
    fun `학생 조회 실패`() {
        // given
        given(queryStudentPort.queryStudentBySchoolIdAndGcn(schoolId, request.grade, request.classRoom, request.number))
            .willReturn(null)

        // when & then
        assertThrows<StudentNotFoundException> {
            findStudentAccountIdUseCase.execute(schoolId, request)
        }
    }

    @Test
    fun `유저 조회 실패`() {
        // given
        given(queryStudentPort.queryStudentBySchoolIdAndGcn(schoolId, request.grade, request.classRoom, request.number))
            .willReturn(student)

        given(queryUserPort.queryByUserId(student.studentId))
            .willReturn(null)

        // when & then
        assertThrows<UserNotFoundException> {
            findStudentAccountIdUseCase.execute(schoolId, request)
        }
    }

    @Test
    fun `학생 이름 불일치`() {
        // given
        given(queryStudentPort.queryStudentBySchoolIdAndGcn(schoolId, request.grade, request.classRoom, request.number))
            .willReturn(student)

        given(queryUserPort.queryByUserId(student.studentId))
            .willReturn(user)

        // when & then
        assertThrows<StudentInfoNotMatchedException> {
            findStudentAccountIdUseCase.execute(schoolId, request.copy(name = "이준서"))
        }
    }
}