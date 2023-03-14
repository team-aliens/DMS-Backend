package team.aliens.dms.domain.studyroom.usecase

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.studyroom.exception.AppliedSeatNotFoundException
import team.aliens.dms.domain.studyroom.exception.SeatNotFoundException
import team.aliens.dms.domain.studyroom.exception.StudyRoomNotFoundException
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.SeatApplication
import team.aliens.dms.domain.studyroom.model.SeatStatus
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.user.model.User
import java.time.LocalDateTime
import java.util.UUID

class QueryCurrentAppliedStudyRoomUseCaseTests {

    private val securityPort: StudyRoomSecurityPort = mockk(relaxed = true)
    private val queryStudyRoomPort: QueryStudyRoomPort = mockk(relaxed = true)

    private val queryCurrentAppliedStudyRoomUseCase = QueryCurrentAppliedStudyRoomUseCase(
        securityPort, queryStudyRoomPort
    )

    private val userId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()

    private val userStub by lazy {
        User(
            id = userId,
            schoolId = schoolId,
            accountId = "test account id",
            password = "test password",
            email = "test email",
            authority = Authority.STUDENT,
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )
    }

    private val seatApplicationStub by lazy {
        SeatApplication(
            seatId = seatStub.id,
            timeSlotId = null,
            studentId = userId
        )
    }

    private val seatStub by lazy {
        Seat(
            id = UUID.randomUUID(),
            studyRoomId = UUID.randomUUID(),
            typeId = UUID.randomUUID(),
            widthLocation = 1,
            heightLocation = 1,
            number = 1,
            status = SeatStatus.AVAILABLE
        )
    }

    private val studyRoomStub by lazy {
        StudyRoom(
            id = UUID.randomUUID(),
            schoolId = userStub.schoolId,
            name = "",
            floor = 1,
            widthSize = 1,
            heightSize = 1,
            availableHeadcount = 1,
            availableSex = Sex.FEMALE,
            availableGrade = 1,
            eastDescription = "",
            westDescription = "",
            southDescription = "",
            northDescription = ""
        )
    }

    @Test
    fun `자습실 목록 조회 성공`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryStudyRoomPort.querySeatApplicationByStudentId(userId) } returns seatApplicationStub
        every { queryStudyRoomPort.querySeatById(seatApplicationStub.seatId) } returns seatStub
        every { queryStudyRoomPort.queryStudyRoomById(seatStub.studyRoomId) } returns studyRoomStub

        // when & then
        assertDoesNotThrow {
            queryCurrentAppliedStudyRoomUseCase.execute()
        }
    }

    @Test
    fun `seatApplication이 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryStudyRoomPort.querySeatApplicationByStudentId(userId) } returns null

        // when & then
        assertThrows<AppliedSeatNotFoundException> {
            queryCurrentAppliedStudyRoomUseCase.execute()
        }
    }

    @Test
    fun `seat이 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryStudyRoomPort.querySeatApplicationByStudentId(userId) } returns seatApplicationStub
        every { queryStudyRoomPort.querySeatById(seatStub.id) } returns null

        // when & then
        assertThrows<SeatNotFoundException> {
            queryCurrentAppliedStudyRoomUseCase.execute()
        }
    }

    @Test
    fun `studyRoom이 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryStudyRoomPort.querySeatApplicationByStudentId(userId) } returns seatApplicationStub
        every { queryStudyRoomPort.querySeatById(seatStub.id) } returns seatStub
        every { queryStudyRoomPort.queryStudyRoomById(seatStub.studyRoomId) } returns null

        // when & then
        assertThrows<StudyRoomNotFoundException> {
            queryCurrentAppliedStudyRoomUseCase.execute()
        }
    }
}
