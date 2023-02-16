package team.aliens.dms.domain.manager.usecase

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.manager.dto.GetStudentDetailsResponse
import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.manager.model.Manager
import team.aliens.dms.domain.manager.spi.ManagerQueryPointPort
import team.aliens.dms.domain.manager.spi.ManagerQueryStudentPort
import team.aliens.dms.domain.manager.spi.ManagerSecurityPort
import team.aliens.dms.domain.manager.spi.QueryManagerPort
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.student.model.Student
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
    private lateinit var queryPointPort: ManagerQueryPointPort

    private lateinit var queryStudentDetailsUseCase: QueryStudentDetailsUseCase

    @BeforeEach
    fun setUp() {
        queryStudentDetailsUseCase = QueryStudentDetailsUseCase(
            securityPort, queryManagerPort, queryStudentPort, queryPointPort
        )
    }

    private val currentUserId = UUID.randomUUID()
    private val studentId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()
    private val bonusPoint = 11
    private val minusPoint = 23

    private val managerStub by lazy {
        Manager(
            id = currentUserId,
            schoolId = schoolId,
            name = "관리자 이름",
            profileImageUrl = "https://~~"
        )
    }

    private val studentStub by lazy {
        Student(
            id = studentId,
            roomId = UUID.randomUUID(),
            roomNumber = 216,
            schoolId = schoolId,
            grade = 2,
            classRoom = 1,
            number = 20,
            name = "김범진",
            profileImageUrl = "profile image url",
            sex = Sex.FEMALE
        )
    }

    private val gcn = studentStub.gcn
    private val name = studentStub.name

    private val responseStub by lazy {
        GetStudentDetailsResponse(
            name = studentStub.name,
            gcn = studentStub.gcn,
            profileImageUrl = studentStub.profileImageUrl ?: Student.PROFILE_IMAGE,
            bonusPoint = bonusPoint,
            minusPoint = minusPoint,
            roomNumber = studentStub.roomNumber,
            roomMates = listOf(
                GetStudentDetailsResponse.RoomMate(
                    id = studentStub.id,
                    name = studentStub.name,
                    profileImageUrl = studentStub.profileImageUrl ?: Student.PROFILE_IMAGE
                )
            )
        )
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

        given(queryPointPort.queryBonusAndMinusTotalPointByGcnAndStudentName(gcn, name))
            .willReturn(Pair(bonusPoint, minusPoint))

        given(queryStudentPort.queryUserByRoomNumberAndSchoolId(studentStub.roomNumber, studentStub.schoolId))
            .willReturn(listOf(studentStub))

        // when
        val response = queryStudentDetailsUseCase.execute(studentId)

        // then
        assertThat(response).isNotNull
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