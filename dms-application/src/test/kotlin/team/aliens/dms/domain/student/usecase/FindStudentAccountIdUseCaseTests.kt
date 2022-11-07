package team.aliens.dms.domain.student.usecase

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.student.dto.FindStudentAccountIdRequest
import team.aliens.dms.domain.student.exception.StudentInfoMismatchException
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.student.spi.StudentQueryUserPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import team.aliens.dms.common.spi.CoveredEmailPort
import java.time.LocalDateTime
import java.util.UUID

@ExtendWith(SpringExtension::class)
class FindStudentAccountIdUseCaseTests {

    @MockBean
    private lateinit var queryStudentPort: QueryStudentPort

    @MockBean
    private lateinit var queryUserPort: StudentQueryUserPort

    @MockBean
    private lateinit var coveredEmailPort: CoveredEmailPort

    private lateinit var findStudentAccountIdUseCase: FindStudentAccountIdUseCase

    @BeforeEach
    fun setUp() {
        findStudentAccountIdUseCase = FindStudentAccountIdUseCase(
            queryStudentPort, queryUserPort, coveredEmailPort
        )
    }

    private val schoolId = UUID.randomUUID()

    private val requestStub by lazy {
        FindStudentAccountIdRequest(
            name = "이정윤",
            grade = 2,
            classRoom = 3,
            number = 10
        )
    }

    private val studentStub by lazy {
        Student(
            studentId = UUID.randomUUID(),
            roomNumber = 318,
            schoolId = schoolId,
            grade = requestStub.grade,
            classRoom = requestStub.classRoom,
            number = requestStub.number
        )
    }

    private val userStub by lazy {
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

    @Test
    fun `아이디 찾기 성공`() {
        // given
        given(queryStudentPort.queryStudentBySchoolIdAndGcn(schoolId, requestStub.grade, requestStub.classRoom, requestStub.number))
            .willReturn(studentStub)

        given(queryUserPort.queryUserById(studentStub.studentId))
            .willReturn(userStub)

        given(coveredEmailPort.coveredEmail(userStub.email))
            .willReturn("dlw********")

        // when
        val response = findStudentAccountIdUseCase.execute(schoolId, requestStub)

        // then
        assertEquals(response, "dlw********")
    }

    @Test
    fun `학생 조회 실패`() {
        // given
        given(queryStudentPort.queryStudentBySchoolIdAndGcn(schoolId, requestStub.grade, requestStub.classRoom, requestStub.number))
            .willReturn(null)

        // when & then
        assertThrows<StudentNotFoundException> {
            findStudentAccountIdUseCase.execute(schoolId, requestStub)
        }
    }

    @Test
    fun `유저 조회 실패`() {
        // given
        given(queryStudentPort.queryStudentBySchoolIdAndGcn(schoolId, requestStub.grade, requestStub.classRoom, requestStub.number))
            .willReturn(studentStub)

        given(queryUserPort.queryUserById(studentStub.studentId))
            .willReturn(null)

        // when & then
        assertThrows<UserNotFoundException> {
            findStudentAccountIdUseCase.execute(schoolId, requestStub)
        }
    }

    @Test
    fun `학생 이름 불일치`() {
        // given
        given(queryStudentPort.queryStudentBySchoolIdAndGcn(schoolId, requestStub.grade, requestStub.classRoom, requestStub.number))
            .willReturn(studentStub)

        given(queryUserPort.queryUserById(studentStub.studentId))
            .willReturn(userStub)

        // when & then
        assertThrows<StudentInfoMismatchException> {
            findStudentAccountIdUseCase.execute(schoolId, requestStub.copy(name = "이준서"))
        }
    }
}