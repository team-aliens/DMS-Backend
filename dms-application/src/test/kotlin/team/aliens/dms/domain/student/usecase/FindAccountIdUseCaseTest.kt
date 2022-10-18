package team.aliens.dms.domain.student.usecase

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
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
class FindAccountIdUseCaseTest {

    @MockBean
    private lateinit var queryStudentPort: QueryStudentPort

    @MockBean
    private lateinit var queryUserPort: StudentQueryUserPort

    @MockBean
    private lateinit var coveredEmailPort: CoveredEmailPort

    private lateinit var findAccountIdUseCase: FindAccountIdUseCase

    private val schoolId = UUID.randomUUID()
    private val name = "이정윤"
    private val grade = 2
    private val classRoom = 3
    private val number = 10

    private val student by lazy {
        Student(
            studentId = UUID.randomUUID(),
            roomNumber = 318,
            schoolId = schoolId,
            grade = grade,
            classRoom = classRoom,
            number = number
        )
    }

    private val user by lazy {
        User(
            id = UUID.randomUUID(),
            schoolId = schoolId,
            accountId = "이정윤123",
            password = "이정윤123!",
            email = "이정윤14@naver.com",
            name = name,
            profileImageUrl = "http~",
            createdAt = LocalDateTime.now(),
            deletedAt = LocalDateTime.now()
        )
    }

    @BeforeEach
    fun setUp() {
        findAccountIdUseCase = FindAccountIdUseCase(queryStudentPort, queryUserPort, coveredEmailPort)
    }

    @Test
    fun `아이디 찾기 성공`() {
        // given
        given(queryStudentPort.queryStudentBySchoolIdAndGcn(schoolId, grade, classRoom, number))
            .willReturn(student)

        given(queryUserPort.queryByStudentId(student.studentId))
            .willReturn(user)

        given(!queryStudentPort.existsByGcn(grade, classRoom, number))
            .willReturn(true)

        given(coveredEmailPort.coveredEmail(user.email))
            .willReturn("dlw********")

        // when
        val response = findAccountIdUseCase.execute(schoolId, name, grade, classRoom, number)

        // then
        assertEquals(response, "dlw********")
    }

    @Test
    fun `학생 조회 실패`() {
        // given
        given(queryStudentPort.queryStudentBySchoolIdAndGcn(schoolId, grade, classRoom, number))
            .willReturn(null)

        // when & then
        assertThrows<StudentNotFoundException> {
            findAccountIdUseCase.execute(schoolId, name, grade, classRoom, number)
        }
    }

    @Test
    fun `유저 조회 실패`() {
        // given
        given(queryStudentPort.queryStudentBySchoolIdAndGcn(schoolId, grade, classRoom, number))
            .willReturn(student)

        given(queryUserPort.queryByStudentId(student.studentId))
            .willReturn(null)

        // when & then
        assertThrows<UserNotFoundException> {
            findAccountIdUseCase.execute(schoolId, name, grade, classRoom, number)
        }
    }

    @Test
    fun `학생 존재하지 않음`() {
        // given
        given(queryStudentPort.queryStudentBySchoolIdAndGcn(schoolId, grade, classRoom, number))
            .willReturn(student)

        given(queryUserPort.queryByStudentId(student.studentId))
            .willReturn(user)

        given(!queryStudentPort.existsByGcn(grade, classRoom, number))
            .willReturn(false)

        // when & then
        assertThrows<StudentInfoNotMatchedException> {
            findAccountIdUseCase.execute(schoolId, name, grade, classRoom, number)
        }
    }
}