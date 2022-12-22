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
import team.aliens.dms.domain.studyroom.exception.StudyRoomNotFoundException
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.SeatStatus
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.spi.QuerySeatTypePort
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryStudentPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort

@ExtendWith(SpringExtension::class)
class QueryStudyRoomStudentUseCaseTests {

    @MockBean
    private lateinit var securityPort: StudyRoomSecurityPort

    @MockBean
    private lateinit var queryStudyRoomPort: QueryStudyRoomPort

    @MockBean
    private lateinit var querySeatTypePort: QuerySeatTypePort

    @MockBean
    private lateinit var queryStudentPort: StudyRoomQueryStudentPort

    private lateinit var queryStudyRoomStudentUseCase: QueryStudyRoomStudentUseCase

    @BeforeEach
    fun setUp() {
        queryStudyRoomStudentUseCase = QueryStudyRoomStudentUseCase(
            securityPort, queryStudyRoomPort, querySeatTypePort, queryStudentPort
        )
    }

    private val studyRoomId = UUID.randomUUID()
    private val currentUserId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()

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

    private val seatStub by lazy {
        Seat(
            id = UUID.randomUUID(),
            studyRoomId = studyRoomId,
            studentId = null,
            typeId = null,
            widthLocation = 1,
            heightLocation = 1,
            number = null,
            status = SeatStatus.IN_USE
        )
    }

    @Test
    fun `자습실 조회 성공`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryStudyRoomPort.queryStudyRoomById(studyRoomId))
            .willReturn(studyRoomStub)

        given(queryStudyRoomPort.countSeatByStudyRoomId(studyRoomId))
            .willReturn(1)

        given(queryStudyRoomPort.queryAllSeatByStudyRoomId(studyRoomId))
            .willReturn(listOf(seatStub))

        given(querySeatTypePort.querySeatTypeId(seatStub.typeId))
            .willReturn(null)

        given(queryStudentPort.queryStudentByIdOrNull(seatStub.studentId))
            .willReturn(null)

        assertDoesNotThrow {
            queryStudyRoomStudentUseCase.execute(studyRoomId)
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
            queryStudyRoomStudentUseCase.execute(studyRoomId)
        }
    }
}