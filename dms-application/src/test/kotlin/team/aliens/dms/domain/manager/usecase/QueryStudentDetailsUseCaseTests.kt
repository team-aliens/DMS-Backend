package team.aliens.dms.domain.manager.usecase

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.manager.spi.ManagerQueryPointHistoryPort
import team.aliens.dms.domain.manager.spi.ManagerQueryStudentPort
import team.aliens.dms.domain.manager.spi.ManagerSecurityPort
import team.aliens.dms.domain.manager.spi.QueryManagerPort
import team.aliens.dms.domain.manager.stub.createManagerStub
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.stub.createStudentStub
import java.util.UUID

@ExtendWith(SpringExtension::class)
class QueryStudentDetailsUseCaseTests {

    @MockBean
    private lateinit var securityPort: ManagerSecurityPort

    @MockBean
    private lateinit var queryManagerPort: QueryManagerPort

    @MockBean
    private lateinit var queryStudentPort: ManagerQueryStudentPort

    @MockBean
    private lateinit var queryPointHistoryPort: ManagerQueryPointHistoryPort

    private lateinit var queryStudentDetailsUseCase: QueryStudentDetailsUseCase

    @BeforeEach
    fun setUp() {
        queryStudentDetailsUseCase = QueryStudentDetailsUseCase(
            securityPort, queryManagerPort, queryStudentPort, queryPointHistoryPort
        )
    }

    private val currentUserId = UUID.randomUUID()
    private val studentId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()
    private val bonusPoint = 11
    private val minusPoint = 23

    private val managerStub by lazy {
        createManagerStub(schoolId = schoolId)
    }

    private val studentStub by lazy {
        createStudentStub(
            id = studentId,
            schoolId = schoolId
        )
    }

    private val gcn = studentStub.gcn
    private val name = studentStub.name

    private val roomMateStub by lazy {
        createStudentStub(schoolId = schoolId)
    }

    @Test
    fun `학생 상세조회 성공`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryManagerPort.queryManagerById(currentUserId))
            .willReturn(managerStub)

        given(queryStudentPort.queryStudentById(studentId))
            .willReturn(studentStub)

        given(queryPointHistoryPort.queryBonusAndMinusTotalPointByStudentGcnAndName(gcn, name))
            .willReturn(Pair(bonusPoint, minusPoint))

        given(queryStudentPort.queryUserByRoomNumberAndSchoolId(studentStub.roomNumber, studentStub.schoolId))
            .willReturn(listOf(studentStub, roomMateStub))

        // when
        val response = queryStudentDetailsUseCase.execute(studentId)

        // then
        assertAll(
            { assertThat(response).isNotNull },
            { assertThat(response.roomMates.isNotEmpty()) },
            { assertEquals(response.roomMates[0].id, roomMateStub.id) },
            { assertEquals(response.roomMates[0].name, roomMateStub.name) },
            { assertEquals(response.roomMates[0].profileImageUrl, roomMateStub.profileImageUrl) }
        )
    }

    @Test
    fun `관리자 미존재`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryManagerPort.queryManagerById(currentUserId))
            .willReturn(null)

        // when & then
        assertThrows<ManagerNotFoundException> {
            queryStudentDetailsUseCase.execute(studentId)
        }
    }

    @Test
    fun `학생 미존재`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryManagerPort.queryManagerById(currentUserId))
            .willReturn(managerStub)

        given(queryStudentPort.queryStudentById(studentId))
            .willReturn(null)

        // when & then
        assertThrows<StudentNotFoundException> {
            queryStudentDetailsUseCase.execute(studentId)
        }
    }

    @Test
    fun `학교 불일치`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryManagerPort.queryManagerById(currentUserId))
            .willReturn(managerStub)

        given(queryStudentPort.queryStudentById(studentId))
            .willReturn(studentStub.copy(schoolId = UUID.randomUUID()))

        // when & then
        assertThrows<SchoolMismatchException> {
            queryStudentDetailsUseCase.execute(studentId)
        }
    }
}
