package team.aliens.dms.domain.point.usecase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.manager.model.Manager
import team.aliens.dms.domain.point.dto.GrantPointRequest
import team.aliens.dms.domain.point.exception.PointOptionNotFoundException
import team.aliens.dms.domain.point.exception.PointOptionSchoolMismatchException
import team.aliens.dms.domain.point.model.PointOption
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.CommandPointHistoryPort
import team.aliens.dms.domain.point.spi.PointQueryManagerPort
import team.aliens.dms.domain.point.spi.PointQueryStudentPort
import team.aliens.dms.domain.point.spi.PointSecurityPort
import team.aliens.dms.domain.point.spi.QueryPointOptionPort
import team.aliens.dms.domain.point.spi.vo.StudentWithPointVO
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import java.util.UUID

@ExtendWith(SpringExtension::class)
class GrantPointUseCaseTest {

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

    private lateinit var grantPointUseCase: GrantPointUseCase

    @BeforeEach
    fun setUp() {
        grantPointUseCase = GrantPointUseCase(
            queryManagerPort, securityPort, queryPointOptionPort, commandPointHistoryPort, queryStudentPort
        )
    }

    private val currentUserId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()
    private val schoolId2 = UUID.randomUUID()
    private val studentId = UUID.randomUUID()
    private val studentId2 = UUID.randomUUID()
    private val pointOptionId = UUID.randomUUID()

    private val studentWithPointVOStub by lazy {
        listOf(
            StudentWithPointVO(
                grade = 2,
                classRoom = 1,
                number = 15,
                name = "이하성",
                bonusTotal = 29,
                minusTotal = 10
            )
        )
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
        GrantPointRequest(
            pointOptionId = pointOptionId,
            studentIdList = listOf(studentId)
        )
    }

    private val requestStubWithInvalidStudent by lazy {
        GrantPointRequest(
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
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryManagerPort.queryManagerById(currentUserId))
            .willReturn(managerStub)

        given(queryPointOptionPort.queryPointOptionById(pointOptionId))
            .willReturn(pointOptionStub)

        given(queryStudentPort.queryStudentsWithPointHistory(requestStub.studentIdList))
            .willReturn(studentWithPointVOStub)

        // when & then
        assertDoesNotThrow {
            grantPointUseCase.execute(requestStub)
        }
    }

    @Test
    fun `상벌점항목이 올바르지 않은 경우`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryManagerPort.queryManagerById(currentUserId))
            .willReturn(managerStub2)

        given(queryStudentPort.queryStudentsWithPointHistory(requestStub.studentIdList))
            .willReturn(studentWithPointVOStub)

        given(queryPointOptionPort.queryPointOptionById(pointOptionId))
            .willReturn(pointOptionStub)

        // when & then
        assertThrows<PointOptionSchoolMismatchException> {
            grantPointUseCase.execute(requestStub)
        }
    }

    @Test
    fun `상벌점항목을 찾을 수 없는 경우`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryManagerPort.queryManagerById(currentUserId))
            .willReturn(managerStub)

        // when & then
        assertThrows<PointOptionNotFoundException> {
            grantPointUseCase.execute(requestStub)
        }
    }

    @Test
    fun `메니저를 찾을 수 없는 경우`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        // when & then
        assertThrows<ManagerNotFoundException> {
            grantPointUseCase.execute(requestStub)
        }
    }

    @Test
    fun `학생을 찾을 수 없는경우`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryManagerPort.queryManagerById(currentUserId))
            .willReturn(managerStub)

        given(queryPointOptionPort.queryPointOptionById(pointOptionId))
            .willReturn(pointOptionStub)

        given(queryStudentPort.queryStudentsWithPointHistory(requestStub.studentIdList))
            .willReturn(studentWithPointVOStub)

        // when & then
        assertThrows<StudentNotFoundException> {
            grantPointUseCase.execute(requestStubWithInvalidStudent)
        }
    }
}
