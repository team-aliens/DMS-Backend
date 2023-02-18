package team.aliens.dms.domain.point.usecase

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import team.aliens.dms.domain.manager.model.Manager
import team.aliens.dms.domain.point.dto.GivePointRequest
import team.aliens.dms.domain.point.model.PointOption
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.*
import team.aliens.dms.domain.point.spi.vo.StudentWithPoint
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import java.util.*

@ExtendWith(SpringExtension::class)
class GivePointUseCaseTest {

    @MockBean
    private lateinit var queryManagerPort: PointQueryManagerPort

    @MockBean
    private lateinit var securityPort: PointSecurityPort

    @MockBean
    private lateinit var queryPointOptionPort: QueryPointOptionPort

    @MockBean
    private lateinit var commandPointHistoryPort: CommandPointHistoryPort

    @MockBean
    private lateinit var queryStudentPort: PointQueryStudentPort

    private lateinit var givePointUseCase: GivePointUseCase

    @BeforeEach
    fun setUp() {
        givePointUseCase = GivePointUseCase(
            queryManagerPort, securityPort, queryPointOptionPort, commandPointHistoryPort, queryStudentPort
        )
    }

    private val currentUserId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()
    private val schoolId2 = UUID.randomUUID()
    private val studentId = UUID.randomUUID()
    private val studentId2 = UUID.randomUUID()
    private val pointOptionId = UUID.randomUUID()

    private val studentWithPointStub by lazy {
        listOf( StudentWithPoint(
            schoolId = schoolId,
            grade = 2,
            classRoom = 1,
            number = 15,
            name = "이하성",
            bonusTotal = 29,
            minusTotal = 10
        ))
    }
    private val managerStub by lazy {
        Manager(
            id = currentUserId,
            schoolId = schoolId,
            name = "이름",
            profileImageUrl = "test"
        )
    }

    private val managerStub2 by lazy {
        Manager(
            id = currentUserId,
            schoolId = schoolId2,
            name = "이름",
            profileImageUrl = "test"
        )
    }

    private val requestStub by lazy {
        GivePointRequest(
            pointOptionId = pointOptionId,
            studentIdList = listOf(studentId)
        )
    }

    private val requestStubWithInvalidStudent by lazy {
        GivePointRequest(
            pointOptionId = pointOptionId,
            studentIdList = listOf(studentId, studentId2)
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

        given(queryPointOptionPort.queryPointOptionByIdAndSchoolId(pointOptionId, schoolId))
            .willReturn(pointOptionStub)

        given(queryStudentPort.queryStudentsWithPointHistory(requestStub.studentIdList))
            .willReturn(studentWithPointStub)

        //when & then
        assertDoesNotThrow {
            givePointUseCase.execute(requestStub)
        }
    }

    @Test
    fun `다른학교 학생일 경우`() {
        //given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryManagerPort.queryManagerById(currentUserId))
            .willReturn(managerStub2)

        given(queryStudentPort.queryStudentsWithPointHistory(requestStub.studentIdList))
            .willReturn(studentWithPointStub)

        given(queryPointOptionPort.queryPointOptionByIdAndSchoolId(pointOptionId, schoolId2))
            .willReturn(pointOptionStub)

        //when & then
        assertThrows<SchoolMismatchException> {
            givePointUseCase.execute(requestStub)
        }
    }

    @Test
    fun `학생을 찾을 수 없는경우`() {
        //given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryManagerPort.queryManagerById(currentUserId))
            .willReturn(managerStub)

        given(queryPointOptionPort.queryPointOptionByIdAndSchoolId(pointOptionId, schoolId))
            .willReturn(pointOptionStub)

        given(queryStudentPort.queryStudentsWithPointHistory(requestStub.studentIdList))
            .willReturn(studentWithPointStub)


        //when & then
        assertThrows<StudentNotFoundException> {
            givePointUseCase.execute(requestStubWithInvalidStudent)
        }
    }
}