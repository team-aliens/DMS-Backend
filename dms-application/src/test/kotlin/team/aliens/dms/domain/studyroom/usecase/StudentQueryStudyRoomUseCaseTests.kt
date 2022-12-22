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
import team.aliens.dms.domain.studyroom.dto.StudentQueryStudyRoomResponse
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

    private val seatResponseStub by lazy {
        StudentQueryStudyRoomResponse.SeatElement(
            id = UUID.randomUUID(),
            widthSize = 1,
            heightSize = 1,
            number = 1,
            type = StudentQueryStudyRoomResponse.SeatElement.TypeElement(
                id = UUID.randomUUID(),
                name = "이름",
                color = "색깔"
            ),
            status = SeatStatus.IN_USE,
            isMine = true,
            student = StudentQueryStudyRoomResponse.SeatElement.StudentElement(
                id = UUID.randomUUID(),
                name = "이름"
            )
        )
    }

    private val seatResponseStudentIdNullStub by lazy {
        StudentQueryStudyRoomResponse.SeatElement(
            id = UUID.randomUUID(),
            widthSize = 1,
            heightSize = 1,
            number = 1,
            type = StudentQueryStudyRoomResponse.SeatElement.TypeElement(
                id = UUID.randomUUID(),
                name = "이름",
                color = "색깔"
            ),
            status = SeatStatus.IN_USE,
            isMine = null,
            student = null
        )
    }

    @Test
    fun `자습실 조회 성공`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryStudyRoomPort.queryStudyRoomById(studyRoomId))
            .willReturn(studyRoomStub)

        given(queryStudyRoomPort.countSeatByStudyRoomIdAndStatus(studyRoomId, SeatStatus.AVAILABLE))
            .willReturn(1)

        given(queryStudyRoomPort.queryAllSeatByStudyRoomId(studyRoomId))
            .willReturn(listOf(seatResponseStub))

        assertDoesNotThrow {
            studentQueryStudyRoomUseCase.execute(studyRoomId)
        }
    }

    @Test
    fun `학생 아이디 NULL`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryStudyRoomPort.queryStudyRoomById(studyRoomId))
            .willReturn(studyRoomStub)

        given(queryStudyRoomPort.countSeatByStudyRoomIdAndStatus(studyRoomId, SeatStatus.AVAILABLE))
            .willReturn(1)

        given(queryStudyRoomPort.queryAllSeatByStudyRoomId(studyRoomId))
            .willReturn(listOf(seatResponseStudentIdNullStub))

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

        assertThrows<StudyRoomNotFoundException> {
            studentQueryStudyRoomUseCase.execute(studyRoomId)
        }
    }
}