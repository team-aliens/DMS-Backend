package team.aliens.dms.domain.studyroom.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.UUID
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import team.aliens.dms.domain.studyroom.exception.TimeSlotNotFoundException
import team.aliens.dms.domain.studyroom.spi.CommandStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.studyroom.stub.createTimeSlotStub
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.stub.createUserStub

class RemoveTimeSlotUseCaseTests {

    private val securityPort: StudyRoomSecurityPort = mockk(relaxed = true)
    private val queryUserPort: StudyRoomQueryUserPort = mockk(relaxed = true)
    private val queryStudyRoomPort: QueryStudyRoomPort = mockk(relaxed = true)
    private val commandStudyRoomPort: CommandStudyRoomPort = mockk(relaxed = true)

    private val removeTimeSlotUseCase = RemoveTimeSlotUseCase(
        securityPort, queryUserPort, queryStudyRoomPort, commandStudyRoomPort
    )

    private val userId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()
    private val timeSlotId = UUID.randomUUID()

    private val userStub by lazy {
        createUserStub(
            id = userId,
            schoolId = schoolId
        )
    }

    private val timeSlotStub by lazy {
        createTimeSlotStub(
            schoolId = schoolId
        )
    }

    @Test
    fun `이용시간 삭제 성공`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryStudyRoomPort.queryTimeSlotById(timeSlotId) } returns timeSlotStub

        // when
        removeTimeSlotUseCase.execute(timeSlotId)

        // then
        verify(exactly = 1) { commandStudyRoomPort.deleteSeatApplicationByTimeSlotId(timeSlotId) }
        verify(exactly = 1) { commandStudyRoomPort.deleteTimeSlotById(timeSlotId) }
    }

    @Test
    fun `유저가 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns null

        // when
        assertThrows<UserNotFoundException> {
            removeTimeSlotUseCase.execute(timeSlotId)
        }
    }

    @Test
    fun `이용시간이 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryStudyRoomPort.queryTimeSlotById(timeSlotId) } returns null

        // when
        assertThrows<TimeSlotNotFoundException> {
            removeTimeSlotUseCase.execute(timeSlotId)
        }
    }
}
