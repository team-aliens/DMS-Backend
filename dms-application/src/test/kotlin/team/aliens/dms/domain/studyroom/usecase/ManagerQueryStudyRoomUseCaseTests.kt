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
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.studyroom.spi.vo.SeatVO
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

    private lateinit var managerQueryStudyRoomUseCase: ManagerQueryStudyRoomUseCase

    @BeforeEach
    fun setUp() {
        managerQueryStudyRoomUseCase = ManagerQueryStudyRoomUseCase(
            securityPort, queryUserPort, queryStudyRoomPort
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
            accountId = "?????? ?????????",
            password = "????????????",
            email = "?????????",
            authority = Authority.STUDENT,
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )
    }

    private val studyRoomStub by lazy {
        StudyRoom(
            id = studyRoomId,
            schoolId = schoolId,
            name = "??????",
            floor = 1,
            widthSize = 1,
            heightSize = 1,
            inUseHeadcount = 1,
            availableHeadcount = 1,
            availableSex = Sex.ALL,
            availableGrade = 1,
            eastDescription = "??????",
            westDescription = "??????",
            southDescription = "??????",
            northDescription = "??????"
        )
    }

    private val managerSeatVOStub by lazy {
        SeatVO(
            seatId = UUID.randomUUID(),
            widthLocation = 1,
            heightLocation = 1,
            number = 1,
            status = SeatStatus.AVAILABLE,
            typeId = UUID.randomUUID(),
            typeName = "?????? ??????",
            typeColor = "??????",
            studentId = studentId,
            studentName = "?????? ??????",
            studentGrade = 1,
            studentClassRoom = 1,
            studentNumber = 1,
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
            name = "?????????",
            profileImageUrl = "https://~",
            sex = Sex.MALE
        )
    }

    @Test
    fun `????????? ?????? ??????`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(userStub)

        given(queryStudyRoomPort.queryStudyRoomById(studyRoomId))
            .willReturn(studyRoomStub)

        given(queryStudyRoomPort.queryAllSeatsByStudyRoomId(studyRoomId))
            .willReturn(listOf(managerSeatVOStub))

        // when & then
        assertDoesNotThrow {
            managerQueryStudyRoomUseCase.execute(studyRoomId)
        }
    }

    @Test
    fun `?????? ?????? ?????? ??????`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(userStub)

        given(queryStudyRoomPort.queryStudyRoomById(studyRoomId))
            .willReturn(studyRoomStub)

        given(queryStudyRoomPort.queryAllSeatsByStudyRoomId(studyRoomId))
            .willReturn(
                listOf(
                    managerSeatVOStub.run {
                        SeatVO(
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
                            studentGrade = null,
                            studentClassRoom = null,
                            studentNumber = null,
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
    fun `?????? ?????? ?????? ?????????`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(userStub)

        given(queryStudyRoomPort.queryStudyRoomById(studyRoomId))
            .willReturn(studyRoomStub)

        given(queryStudyRoomPort.queryAllSeatsByStudyRoomId(studyRoomId))
            .willReturn(
                listOf(
                    managerSeatVOStub.run {
                        SeatVO(
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
                            studentGrade = null,
                            studentClassRoom = null,
                            studentNumber = null,
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
    fun `?????? ?????? ?????????`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(userStub)

        given(queryStudyRoomPort.queryStudyRoomById(studyRoomId))
            .willReturn(studyRoomStub)

        given(queryStudyRoomPort.queryAllSeatsByStudyRoomId(studyRoomId))
            .willReturn(
                listOf(
                    managerSeatVOStub.run {
                        SeatVO(
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
                            studentGrade = studentGrade,
                            studentClassRoom = studentClassRoom,
                            studentNumber = studentNumber,
                            studentProfileImageUrl = studentProfileImageUrl
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
    fun `?????? ?????? ??? ??????`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(userStub)

        given(queryStudyRoomPort.queryStudyRoomById(studyRoomId))
            .willReturn(studyRoomStub)

        given(queryStudyRoomPort.queryAllSeatsByStudyRoomId(studyRoomId))
            .willReturn(
                listOf(
                    managerSeatVOStub.run {
                        SeatVO(
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
                            studentGrade = null,
                            studentClassRoom = null,
                            studentNumber = null,
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
    fun `????????? ?????????`() {
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
    fun `????????? ?????????`() {
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
    fun `?????? ?????????`() {
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