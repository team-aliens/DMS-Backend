package team.aliens.dms.domain.studyroom.usecase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.studyroom.exception.AppliedSeatNotFound
import team.aliens.dms.domain.studyroom.exception.StudyRoomNotFoundException
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.SeatStatus
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import java.util.UUID

@ExtendWith(SpringExtension::class)
class StudentQueryMyStudyRoomUseCaseTests {

    @MockBean
    private lateinit var securityPort: StudyRoomSecurityPort

    @MockBean
    private lateinit var queryStudyRoomPort: QueryStudyRoomPort

    private lateinit var studentQueryMyStudyRoomUseCase: StudentQueryMyStudyRoomUseCase

    @BeforeEach
    fun setUp() {
        studentQueryMyStudyRoomUseCase = StudentQueryMyStudyRoomUseCase(
            securityPort, queryStudyRoomPort
        )
    }

    private val currentUserId = UUID.randomUUID()
    private val studyRoomId = UUID.randomUUID()

    private val seatStub by lazy {
        Seat(
            id = UUID.randomUUID(),
            studyRoomId = studyRoomId,
            studentId = currentUserId,
            typeId = null,
            widthLocation = 1,
            heightLocation = 1,
            number = 1,
            status = SeatStatus.IN_USE
        )
    }

    private val studyRoomStub by lazy {
        StudyRoom(
            schoolId = UUID.randomUUID(),
            name = "가온실",
            floor = 1,
            widthSize = 10,
            heightSize = 10,
            inUseHeadcount = 1,
            availableHeadcount = 10,
            availableSex = Sex.ALL,
            availableGrade = 1,
            eastDescription = "eastDescription",
            westDescription = "westDescription",
            southDescription = "southDescription",
            northDescription = "northDescription"
        )
    }

    @Test
    fun `내 자습실 신청항목 조회 성공`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryStudyRoomPort.querySeatByStudentId(currentUserId))
            .willReturn(seatStub)

        given(queryStudyRoomPort.queryStudyRoomById(studyRoomId))
            .willReturn(studyRoomStub)

        // when & then
        assertDoesNotThrow {
            studentQueryMyStudyRoomUseCase.execute()
        }
    }

    @Test
    fun `자습실 자리를 신청하지 않음`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryStudyRoomPort.querySeatByStudentId(currentUserId))
            .willReturn(null)

        // when & then
        assertThrows<AppliedSeatNotFound> {
            studentQueryMyStudyRoomUseCase.execute()
        }
    }

    @Test
    fun `자습실을 찾지 못함`() {
        //given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryStudyRoomPort.querySeatByStudentId(currentUserId))
            .willReturn(seatStub)

        given(queryStudyRoomPort.queryStudyRoomById(studyRoomId))
            .willReturn(null)

        //when & then
        assertThrows<StudyRoomNotFoundException> {
            studentQueryMyStudyRoomUseCase.execute()
        }
    }
}