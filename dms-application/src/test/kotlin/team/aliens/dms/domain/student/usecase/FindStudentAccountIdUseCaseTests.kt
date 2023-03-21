package team.aliens.dms.domain.student.usecase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.spi.SendEmailPort
import team.aliens.dms.domain.student.dto.FindStudentAccountIdRequest
import team.aliens.dms.domain.student.exception.StudentInfoMismatchException
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.spi.QueryStudentPort
import team.aliens.dms.domain.student.spi.StudentQueryUserPort
import team.aliens.dms.domain.student.stub.createStudentStub
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.stub.createUserStub
import java.util.UUID

@ExtendWith(SpringExtension::class)
class FindStudentAccountIdUseCaseTests {

    @MockBean
    private lateinit var queryStudentPort: QueryStudentPort

    @MockBean
    private lateinit var queryUserPort: StudentQueryUserPort

    @MockBean
    private lateinit var sendEmailPort: SendEmailPort

    private lateinit var findStudentAccountIdUseCase: FindStudentAccountIdUseCase

    @BeforeEach
    fun setUp() {
        findStudentAccountIdUseCase = FindStudentAccountIdUseCase(
            queryStudentPort, queryUserPort, sendEmailPort
        )
    }

    private val schoolId = UUID.randomUUID()

    private val requestStub by lazy {
        FindStudentAccountIdRequest(
            name = "이름",
            grade = 2,
            classRoom = 2,
            number = 1
        )
    }

    private val studentStub by lazy {
        createStudentStub()
    }

    private val userStub by lazy {
        createUserStub(schoolId = schoolId)
    }

    @Test
    fun `아이디 찾기 성공`() {
        // given
        given(
            queryStudentPort.queryStudentBySchoolIdAndGcn(
                schoolId,
                requestStub.grade,
                requestStub.classRoom,
                requestStub.number
            )
        )
            .willReturn(studentStub)

        given(queryUserPort.queryUserById(studentStub.id))
            .willReturn(userStub)

        // when & then
        assertDoesNotThrow {
            findStudentAccountIdUseCase.execute(schoolId, requestStub)
        }
    }

    @Test
    fun `학생 조회 실패`() {
        // given
        given(
            queryStudentPort.queryStudentBySchoolIdAndGcn(
                schoolId,
                requestStub.grade,
                requestStub.classRoom,
                requestStub.number
            )
        )
            .willReturn(null)

        // when & then
        assertThrows<StudentNotFoundException> {
            findStudentAccountIdUseCase.execute(schoolId, requestStub)
        }
    }

    @Test
    fun `유저 조회 실패`() {
        // given
        given(
            queryStudentPort.queryStudentBySchoolIdAndGcn(
                schoolId,
                requestStub.grade,
                requestStub.classRoom,
                requestStub.number
            )
        )
            .willReturn(studentStub)

        given(queryUserPort.queryUserById(studentStub.id))
            .willReturn(null)

        // when & then
        assertThrows<UserNotFoundException> {
            findStudentAccountIdUseCase.execute(schoolId, requestStub)
        }
    }

    @Test
    fun `학생 이름 불일치`() {
        // given
        given(
            queryStudentPort.queryStudentBySchoolIdAndGcn(
                schoolId,
                requestStub.grade,
                requestStub.classRoom,
                requestStub.number
            )
        )
            .willReturn(studentStub)

        given(queryUserPort.queryUserById(studentStub.id))
            .willReturn(userStub)

        // when & then
        assertThrows<StudentInfoMismatchException> {
            findStudentAccountIdUseCase.execute(schoolId, requestStub.copy(name = "이준서"))
        }
    }
}
