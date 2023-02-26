package team.aliens.dms.domain.studyroom.usecase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.SeatStatus
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.studyroom.spi.vo.StudyRoomVO
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import java.time.LocalDateTime
import java.util.UUID

@ExtendWith(SpringExtension::class)
class ManagerQueryStudyRoomsUseCaseTests {

    @MockBean
    private lateinit var securityPort: StudyRoomSecurityPort

    @MockBean
    private lateinit var queryUserPort: StudyRoomQueryUserPort

    @MockBean
    private lateinit var queryStudyRoomPort: QueryStudyRoomPort

    private lateinit var managerQueryStudyRoomsUseCase: ManagerQueryStudyRoomsUseCase

    @BeforeEach
    fun setUp() {
        managerQueryStudyRoomsUseCase = ManagerQueryStudyRoomsUseCase(
            securityPort, queryUserPort, queryStudyRoomPort
        )
    }

    private val currentUserId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()
    private val studyRoomId = UUID.randomUUID()

    private val userStub by lazy {
        User(
            id = currentUserId,
            schoolId = schoolId,
            accountId = "계정 아이디",
            password = "비밀번호",
            email = "이메일",
            authority = Authority.STUDENT,
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )
    }

    private val seatStub by lazy {
        Seat(
            id = UUID.randomUUID(),
            studyRoomId = studyRoomId,
            studentId = currentUserId,
            typeId = UUID.randomUUID(),
            widthLocation = 1,
            heightLocation = 1,
            number = 1,
            status = SeatStatus.AVAILABLE
        )
    }

    private val studyRoomVOStub by lazy {
        StudyRoomVO(
            id = UUID.randomUUID(),
            floor = 1,
            name = "다온실",
            availableGrade = 1,
            availableSex = Sex.MALE,
            inUseHeadcount = 1,
            totalAvailableSeat = 1
        )
    }

    @Test
    fun `학생 자습실 조회 성공`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(userStub)

        given(queryStudyRoomPort.querySeatByStudentId(currentUserId))
            .willReturn(seatStub)

        given(queryStudyRoomPort.queryAllStudyRoomsBySchoolId(userStub.schoolId))
            .willReturn(listOf(studyRoomVOStub))

        // when & then
        assertDoesNotThrow {
            managerQueryStudyRoomsUseCase.execute()
        }
    }

    @Test
    fun `사용자 미존재`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(null)

        // when & then
        assertThrows<UserNotFoundException> {
            managerQueryStudyRoomsUseCase.execute()
        }
    }
}
