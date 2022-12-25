package team.aliens.dms.domain.studyroom.usecase

import java.time.LocalDateTime
import java.util.UUID
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.studyroom.exception.StudyRoomNotFoundException
import team.aliens.dms.domain.studyroom.model.SeatStatus
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryStudentPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.studyroom.spi.vo.ManagerSeatVO
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User

@ExtendWith(SpringExtension::class)
class ManagerQueryStudyRoomUseCaseTests {

    @MockBean
    private lateinit var securityPort: StudyRoomSecurityPort

    @MockBean
    private lateinit var queryUserPort: StudyRoomQueryUserPort

    @MockBean
    private lateinit var queryStudyRoomPort: QueryStudyRoomPort

    @MockBean
    private lateinit var queryStudentPort: StudyRoomQueryStudentPort

    private lateinit var managerQueryStudyRoomUseCase: ManagerQueryStudyRoomUseCase

    @BeforeEach
    fun setUp() {
        managerQueryStudyRoomUseCase = ManagerQueryStudyRoomUseCase(
            securityPort, queryUserPort, queryStudyRoomPort, queryStudentPort
        )
    }

    private val studyRoomId = UUID.randomUUID()
    private val currentUserId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()
    private val studentId = UUID.randomUUID()

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

    private val studyRoomStub by lazy {
        StudyRoom(
            id = studyRoomId,
            schoolId = schoolId,
            name = "이름",
            floor = 1,
            widthSize = 1,
            heightSize = 1,
            inUseHeadcount = 1,
            availableHeadcount = 1,
            availableSex = Sex.ALL,
            availableGrade = 1,
            eastDescription = "동쪽",
            westDescription = "서쪽",
            southDescription = "남쪽",
            northDescription = "북쪽"
        )
    }

    private val managerSeatVOStub by lazy {
        ManagerSeatVO(
            seatId = UUID.randomUUID(),
            widthLocation = 1,
            heightLocation = 1,
            number = 1,
            status = SeatStatus.AVAILABLE,
            typeId = UUID.randomUUID(),
            typeName = "타입 이름",
            typeColor = "색깔",
            studentId = studentId,
            studentName = "학생 이름",
            studentProfileImageUrl = "https://~"
        )
    }

    private val studentStub by lazy {
        Student(
            id = studentId,
            roomId = UUID.randomUUID(),
            roomNumber = 1,
            schoolId = schoolId,
            grade = 1,
            classRoom = 1,
            number = 1,
            name = "이정윤",
            profileImageUrl = "https://~",
            sex = Sex.MALE
        )
    }

    @Test
    fun `자습실 조회 성공`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(userStub)

        given(queryStudyRoomPort.queryStudyRoomById(studyRoomId))
            .willReturn(studyRoomStub)

        given(queryStudyRoomPort.queryAllManagerSeatsByStudyRoomId(studyRoomId))
            .willReturn(listOf(managerSeatVOStub))

        given(queryStudentPort.queryStudentById(studentId))
            .willReturn(studentStub)

        // when & then
        assertDoesNotThrow {
            managerQueryStudyRoomUseCase.execute(studyRoomId)
        }
    }

    @Test
    fun `자리 상태 신청 가능`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(userStub)

        given(queryStudyRoomPort.queryStudyRoomById(studyRoomId))
            .willReturn(studyRoomStub)

        given(queryStudyRoomPort.queryAllManagerSeatsByStudyRoomId(studyRoomId))
            .willReturn(
                listOf(
                    managerSeatVOStub.run {
                        ManagerSeatVO(
                            seatId = seatId,
                            widthLocation = widthLocation,
                            heightLocation = heightLocation,
                            number = number,
                            status = SeatStatus.AVAILABLE,
                            typeId = typeId,
                            typeName = typeName,
                            typeColor = typeColor,
                            studentId = null,
                            studentName = null,
                            studentProfileImageUrl = null
                        )
                    }
                )
            )

        // when & then
        assertDoesNotThrow {
            managerQueryStudyRoomUseCase.execute(studyRoomId)
        }
    }

    @Test
    fun `자리 상태 신청 불가능`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(userStub)

        given(queryStudyRoomPort.queryStudyRoomById(studyRoomId))
            .willReturn(studyRoomStub)

        given(queryStudyRoomPort.queryAllManagerSeatsByStudyRoomId(studyRoomId))
            .willReturn(
                listOf(
                    managerSeatVOStub.run {
                        ManagerSeatVO(
                            seatId = seatId,
                            widthLocation = widthLocation,
                            heightLocation = heightLocation,
                            number = null,
                            status = SeatStatus.UNAVAILABLE,
                            typeId = null,
                            typeName = null,
                            typeColor = null,
                            studentId = null,
                            studentName = null,
                            studentProfileImageUrl = null
                        )
                    }
                )
            )

        // when & then
        assertDoesNotThrow {
            managerQueryStudyRoomUseCase.execute(studyRoomId)
        }
    }

    @Test
    fun `자리 상태 사용중`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(userStub)

        given(queryStudyRoomPort.queryStudyRoomById(studyRoomId))
            .willReturn(studyRoomStub)

        given(queryStudyRoomPort.queryAllManagerSeatsByStudyRoomId(studyRoomId))
            .willReturn(
                listOf(
                    managerSeatVOStub.run {
                        ManagerSeatVO(
                            seatId = seatId,
                            widthLocation = widthLocation,
                            heightLocation = heightLocation,
                            number = number,
                            status = SeatStatus.IN_USE,
                            typeId = typeId,
                            typeName = typeName,
                            typeColor = typeColor,
                            studentId = studentId,
                            studentName = studentName,
                            studentProfileImageUrl = studentProfileImageUrl
                        )
                    }
                )
            )

        given(queryStudentPort.queryStudentById(studentId))
            .willReturn(studentStub)

        // when & then
        assertDoesNotThrow {
            managerQueryStudyRoomUseCase.execute(studyRoomId)
        }
    }

    @Test
    fun `자리 상태 빈 자리`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(userStub)

        given(queryStudyRoomPort.queryStudyRoomById(studyRoomId))
            .willReturn(studyRoomStub)

        given(queryStudyRoomPort.queryAllManagerSeatsByStudyRoomId(studyRoomId))
            .willReturn(
                listOf(
                    managerSeatVOStub.run {
                        ManagerSeatVO(
                            seatId = seatId,
                            widthLocation = widthLocation,
                            heightLocation = heightLocation,
                            number = null,
                            status = SeatStatus.EMPTY,
                            typeId = null,
                            typeName = null,
                            typeColor = null,
                            studentId = null,
                            studentName = null,
                            studentProfileImageUrl = null
                        )
                    }
                )
            )

        // when & then
        assertDoesNotThrow {
            managerQueryStudyRoomUseCase.execute(studyRoomId)
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
            managerQueryStudyRoomUseCase.execute(studyRoomId)
        }
    }

    @Test
    fun `자습실 미존재`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(userStub)

        given(queryStudyRoomPort.queryStudyRoomById(studyRoomId))
            .willReturn(null)

        // when & then
        assertThrows<StudyRoomNotFoundException> {
            managerQueryStudyRoomUseCase.execute(studyRoomId)
        }
    }

    @Test
    fun `학교 불일치`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(userStub)

        given(queryStudyRoomPort.queryStudyRoomById(studyRoomId))
            .willReturn(studyRoomStub.copy(schoolId = UUID.randomUUID()))

        // when & then
        assertThrows<SchoolMismatchException> {
            managerQueryStudyRoomUseCase.execute(studyRoomId)
        }
    }
}