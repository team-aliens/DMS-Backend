package team.aliens.dms.domain.point.usecase

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.manager.spi.QueryManagerPort
import team.aliens.dms.domain.point.spi.PointQueryStudentPort
import team.aliens.dms.domain.point.spi.PointSecurityPort
import team.aliens.dms.domain.point.spi.QueryPointPort
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import team.aliens.dms.domain.manager.model.Manager
import team.aliens.dms.domain.point.dto.ApplyPointRequest
import team.aliens.dms.domain.point.model.PointOption
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.CommandPointPort
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.student.model.Student
import java.util.*

@ExtendWith(SpringExtension::class)
class ApplyPointUseCaseTest {

    @MockBean
    private lateinit var queryManagerPort: QueryManagerPort

    @MockBean
    private lateinit var securityPort: PointSecurityPort

    @MockBean
    private lateinit var queryPointPort: QueryPointPort

    @MockBean
    private lateinit var commandPointPort: CommandPointPort

    @MockBean
    private lateinit var queryStudentPort: PointQueryStudentPort

    private lateinit var applyPointUseCase: ApplyPointUseCase

    @BeforeEach
    fun setUp() {
        applyPointUseCase = ApplyPointUseCase(
            queryManagerPort, securityPort, queryPointPort, commandPointPort, queryStudentPort
        )
    }

    private val currentUserId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()
    private val schoolId2 = UUID.randomUUID()
    private val studentId = UUID.randomUUID()
    private val roomId = UUID.randomUUID()
    private val pointOptionId = UUID.randomUUID()

    private val managerStub by lazy {
        Manager(
            id = currentUserId,
            schoolId = schoolId,
            name = "관리자 이름",
            profileImageUrl = "test"
        )
    }

    private val studentStub by lazy {
        Student(
            id = studentId,
            roomId = roomId,
            roomNumber = 422,
            grade = 2,
            classRoom = 1,
            number = 15,
            name = "이하성",
            schoolId = schoolId2,
            sex = Sex.MALE
        )
    }

    private val studentStub2 by lazy {
        Student(
            id = studentId,
            roomId = roomId,
            roomNumber = 422,
            grade = 2,
            classRoom = 1,
            number = 15,
            name = "이하성",
            schoolId = schoolId,
            sex = Sex.MALE
        )
    }

    private val requestStub by lazy {
        ApplyPointRequest(
            pointOptionId = pointOptionId,
            studentIdList = listOf(studentId)
        )
    }

    private val pointOptionStub by lazy {
        PointOption(
            id = pointOptionId,
            schoolId = schoolId,
            type = PointType.BONUS,
            name = "봉사",
            score = 10,
        )
    }

    @Test
    fun `상벌점부여 성공`() {
        //given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryManagerPort.queryManagerById(currentUserId))
            .willReturn(managerStub)

        given(queryStudentPort.queryStudentById(studentId))
            .willReturn(studentStub2)

        given(queryPointPort.queryPointOptionByIdAndSchoolId(pointOptionId, schoolId))
            .willReturn(pointOptionStub)

        given(queryPointPort.queryBonusAndMinusTotalPointByStudentGcnAndName(studentStub2.gcn, studentStub2.name))
            .willReturn(Pair(10, 10))

        //when & then
        assertDoesNotThrow {
            applyPointUseCase.execute(requestStub)
        }
    }

    @Test
    fun `다른학교 학생일 경우`() {
        //given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryManagerPort.queryManagerById(currentUserId))
            .willReturn(managerStub)

        given(queryStudentPort.queryStudentById(studentId))
            .willReturn(studentStub)

        given(queryPointPort.queryPointOptionByIdAndSchoolId(pointOptionId, schoolId))
            .willReturn(pointOptionStub)

        //when & then
        assertThrows<SchoolMismatchException> {
            applyPointUseCase.execute(requestStub)
        }
    }

    @Test
    fun `학생을 찾을 수 없는경우`() {
        //given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryManagerPort.queryManagerById(currentUserId))
            .willReturn(managerStub)

        given(queryPointPort.queryPointOptionByIdAndSchoolId(pointOptionId, schoolId))
            .willReturn(pointOptionStub)

        //when & then
        assertThrows<StudentNotFoundException> {
            applyPointUseCase.execute(requestStub)
        }
    }
}