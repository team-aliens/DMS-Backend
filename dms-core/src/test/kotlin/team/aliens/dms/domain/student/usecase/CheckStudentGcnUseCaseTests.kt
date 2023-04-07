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
import team.aliens.dms.domain.school.stub.createSchoolStub
import team.aliens.dms.domain.student.dto.CheckStudentGcnRequest
import team.aliens.dms.domain.student.exception.VerifiedStudentNotFoundException
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.student.spi.StudentQuerySchoolPort
import team.aliens.dms.domain.student.spi.StudentQueryVerifiedStudentPort
import team.aliens.dms.domain.student.stub.createVerifiedStudentStub
import java.util.UUID

@ExtendWith(SpringExtension::class)
class CheckStudentGcnUseCaseTests {

    @MockBean
    private lateinit var querySchoolPort: StudentQuerySchoolPort

    @MockBean
    private lateinit var queryVerifiedStudentPort: StudentQueryVerifiedStudentPort

    private lateinit var checkStudentGcnUseCase: CheckStudentGcnUseCase

    @BeforeEach
    fun setUp() {
        checkStudentGcnUseCase = CheckStudentGcnUseCase(
            querySchoolPort, queryVerifiedStudentPort
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
        createSchoolStub()
    }

    private val verifiedStudent by lazy {
        createVerifiedStudentStub(schoolName = schoolStub.name)
    }

    @Test
    fun `학번 확인 성공`() {
        // given
        given(querySchoolPort.querySchoolById(requestStub.schoolId))
            .willReturn(schoolStub)

        given(
            queryVerifiedStudentPort.queryVerifiedStudentByGcnAndSchoolName(
                gcn = Student.processGcn(requestStub.grade, requestStub.classRoom, requestStub.number),
                schoolName = schoolStub.name
            )
        )
            .willReturn(verifiedStudent)

        // when
        val response = checkStudentGcnUseCase.execute(requestStub)

        // then
        assertEquals(response, verifiedStudent.name)
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

        given(
            queryVerifiedStudentPort.queryVerifiedStudentByGcnAndSchoolName(
                gcn = Student.processGcn(requestStub.grade, requestStub.classRoom, requestStub.number),
                schoolName = schoolStub.name
            )
        )
            .willReturn(null)

        // when & then
        assertThrows<VerifiedStudentNotFoundException> {
            checkStudentGcnUseCase.execute(requestStub)
        }
    }
}
