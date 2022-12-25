package team.aliens.dms.domain.studyroom.usecase

import java.util.UUID
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.studyroom.spi.vo.SeatVO
import team.aliens.dms.domain.studyroom.exception.StudyRoomNotFoundException
import team.aliens.dms.domain.studyroom.model.SeatStatus
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort

@ExtendWith(SpringExtension::class)
class StudentQueryStudyRoomUseCaseTests {

    @MockBean
    private lateinit var securityPort: StudyRoomSecurityPort

    @MockBean
    private lateinit var queryStudyRoomPort: QueryStudyRoomPort

    private lateinit var studentQueryStudyRoomUseCase: StudentQueryStudyRoomUseCase

    @BeforeEach
    fun setUp() {
        studentQueryStudyRoomUseCase = StudentQueryStudyRoomUseCase(
            securityPort, queryStudyRoomPort
        )
    }

    private val studyRoomId = UUID.randomUUID()
    private val currentUserId = UUID.randomUUID()

    private val studyRoomStub by lazy {
        StudyRoom(
            id = studyRoomId,
            schoolId = UUID.randomUUID(),
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

    private val seatVOStub by lazy {
        SeatVO(
            seatId = UUID.randomUUID(),
            widthLocation = 1,
            heightLocation = 1,
            number = 1,
            status = SeatStatus.AVAILABLE,
            typeId = UUID.randomUUID(),
            typeName = "타입 이름",
            typeColor = "색깔",
            studentId = UUID.randomUUID(),
            studentName = "학생 이름"
        )
    }

    @Test
    fun `자습실 조회 성공`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryStudyRoomPort.queryStudyRoomById(studyRoomId))
            .willReturn(studyRoomStub)

        given(queryStudyRoomPort.queryAllSeatByStudyRoomId(studyRoomId))
            .willReturn(listOf(seatVOStub))

        // when & then
        assertDoesNotThrow {
            studentQueryStudyRoomUseCase.execute(studyRoomId)
        }
    }

    @Test
    fun `자리 상태 신청 가능`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryStudyRoomPort.queryStudyRoomById(studyRoomId))
            .willReturn(studyRoomStub)

        given(queryStudyRoomPort.queryAllSeatByStudyRoomId(studyRoomId))
            .willReturn(
                listOf(
                    seatVOStub.run {
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
                            studentName = null
                        )
                    }
                )
            )

        // when & then
        assertDoesNotThrow {
            studentQueryStudyRoomUseCase.execute(studyRoomId)
        }
    }

    @Test
    fun `자리 상태 신청 불가능`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryStudyRoomPort.queryStudyRoomById(studyRoomId))
            .willReturn(studyRoomStub)

        given(queryStudyRoomPort.queryAllSeatByStudyRoomId(studyRoomId))
            .willReturn(
                listOf(
                    seatVOStub.run {
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
                            studentName = null
                        )
                    }
                )
            )

        // when & then
        assertDoesNotThrow {
            studentQueryStudyRoomUseCase.execute(studyRoomId)
        }
    }

    @Test
    fun `자리 상태 사용중`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryStudyRoomPort.queryStudyRoomById(studyRoomId))
            .willReturn(studyRoomStub)

        given(queryStudyRoomPort.queryAllSeatByStudyRoomId(studyRoomId))
            .willReturn(
                listOf(
                    seatVOStub.run {
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
                            studentName = studentName
                        )
                    }
                )
            )

        // when & then
        assertDoesNotThrow {
            studentQueryStudyRoomUseCase.execute(studyRoomId)
        }
    }

    @Test
    fun `자리 상태 빈 자리`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryStudyRoomPort.queryStudyRoomById(studyRoomId))
            .willReturn(studyRoomStub)

        given(queryStudyRoomPort.queryAllSeatByStudyRoomId(studyRoomId))
            .willReturn(
                listOf(
                    seatVOStub.run {
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
                            studentName = null
                        )
                    }
                )
            )

        // when & then
        assertDoesNotThrow {
            studentQueryStudyRoomUseCase.execute(studyRoomId)
        }
    }

    @Test
    fun `자습실 미존재`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryStudyRoomPort.queryStudyRoomById(studyRoomId))
            .willReturn(null)

        // when & then
        assertThrows<StudyRoomNotFoundException> {
            studentQueryStudyRoomUseCase.execute(studyRoomId)
        }
    }
}