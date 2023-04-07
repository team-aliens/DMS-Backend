package team.aliens.dms.domain.studyroom.usecase

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import team.aliens.dms.domain.studyroom.exception.AppliedSeatNotFoundException
import team.aliens.dms.domain.studyroom.exception.SeatNotFoundException
import team.aliens.dms.domain.studyroom.exception.StudyRoomNotFoundException
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.studyroom.stub.createSeatApplicationStub
import team.aliens.dms.domain.studyroom.stub.createSeatStub
import team.aliens.dms.domain.studyroom.stub.createStudyRoomStub
import team.aliens.dms.domain.user.stub.createUserStub
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
        createUserStub(
            id = userId,
            schoolId = schoolId
        )
    }

    private val seatApplicationStub by lazy {
        createSeatApplicationStub(
            seatId = seatStub.id,
            timeSlotId = UUID.randomUUID(),
            studentId = userId
        )
    }

    private val seatStub by lazy {
        createSeatStub()
    }

    private val studyRoomStub by lazy {
        createStudyRoomStub(
            schoolId = userStub.schoolId
        )
    }

    @Test
    fun `자습실 목록 조회 성공`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryStudyRoomPort.querySeatApplicationsByStudentId(userId) } returns listOf(seatApplicationStub)
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
        every { queryStudyRoomPort.querySeatApplicationsByStudentId(userId) } returns listOf()

        // when & then
        assertThrows<AppliedSeatNotFoundException> {
            queryCurrentAppliedStudyRoomUseCase.execute()
        }
    }

    @Test
    fun `seat이 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryStudyRoomPort.querySeatApplicationsByStudentId(userId) } returns listOf(seatApplicationStub)
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
        every { queryStudyRoomPort.querySeatApplicationsByStudentId(userId) } returns listOf(seatApplicationStub)
        every { queryStudyRoomPort.querySeatById(seatStub.id) } returns seatStub
        every { queryStudyRoomPort.queryStudyRoomById(seatStub.studyRoomId) } returns null

        // when & then
        assertThrows<StudyRoomNotFoundException> {
            queryCurrentAppliedStudyRoomUseCase.execute()
        }
    }
}
