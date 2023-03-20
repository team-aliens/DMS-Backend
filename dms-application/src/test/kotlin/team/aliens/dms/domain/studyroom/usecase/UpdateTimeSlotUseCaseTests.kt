package team.aliens.dms.domain.studyroom.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.studyroom.exception.TimeSlotAlreadyExistsException
import team.aliens.dms.domain.studyroom.exception.TimeSlotNotFoundException
import team.aliens.dms.domain.studyroom.model.TimeSlot
import team.aliens.dms.domain.studyroom.spi.CommandStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User

class UpdateTimeSlotUseCaseTests {

    private val securityPort: StudyRoomSecurityPort = mockk(relaxed = true)
    private val queryUserPort: StudyRoomQueryUserPort = mockk(relaxed = true)
    private val queryStudyRoomPort: QueryStudyRoomPort = mockk(relaxed = true)
    private val commandStudyRoomPort: CommandStudyRoomPort = mockk(relaxed = true)

    private val updateTimeSlotUseCase = UpdateTimeSlotUseCase(
        securityPort, queryUserPort, queryStudyRoomPort, commandStudyRoomPort
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

    private val timeSlotStub by lazy {
        TimeSlot(
            id = UUID.randomUUID(),
            schoolId = schoolId,
            startTime = LocalTime.of(0, 0),
            endTime = LocalTime.of(0, 0)
        )
    }
    private val newStartTime = LocalTime.of(1, 1)
    private val newEndTime = LocalTime.of(1, 1)

    @Test
    fun `이용시간 수정 성공`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryStudyRoomPort.existsTimeSlotByStartTimeAndEndTime(newStartTime, newEndTime) } returns false
        every { queryStudyRoomPort.queryTimeSlotById(timeSlotStub.id) } returns timeSlotStub

        val newTimeSlot = slot<TimeSlot>()
        every { commandStudyRoomPort.saveTimeSlot(capture(newTimeSlot)) } returnsArgument 0

        // when & then
        assertAll(
            { assertDoesNotThrow { updateTimeSlotUseCase.execute(timeSlotStub.id, newStartTime, newEndTime) } },
            { assertNotEquals(timeSlotStub.startTime, newStartTime) },
            { assertNotEquals(timeSlotStub.endTime, newEndTime) }
        )
    }

    @Test
    fun `유저가 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns null

        // when & then
        assertThrows<UserNotFoundException> {
            updateTimeSlotUseCase.execute(timeSlotStub.id, newStartTime, newEndTime)
        }
    }

    @Test
    fun `이미 존재하는 시간대임`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryStudyRoomPort.existsTimeSlotByStartTimeAndEndTime(newStartTime, newEndTime) } returns true

        // when & then
        assertThrows<TimeSlotAlreadyExistsException> {
            updateTimeSlotUseCase.execute(timeSlotStub.id, newStartTime, newEndTime)
        }
    }

    @Test
    fun `이용시간이 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryStudyRoomPort.existsTimeSlotByStartTimeAndEndTime(newStartTime, newEndTime) } returns false
        every { queryStudyRoomPort.queryTimeSlotById(timeSlotStub.id) } returns null

        // when & then
        assertThrows<TimeSlotNotFoundException> {
            updateTimeSlotUseCase.execute(timeSlotStub.id, newStartTime, newEndTime)
        }
    }

    private val otherTimeSlotStub by lazy {
        TimeSlot(
            id = UUID.randomUUID(),
            schoolId = UUID.randomUUID(),
            startTime = LocalTime.of(0, 0),
            endTime = LocalTime.of(0, 0)
        )
    }

    @Test
    fun `학교가 일치하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryStudyRoomPort.existsTimeSlotByStartTimeAndEndTime(newStartTime, newEndTime) } returns false
        every { queryStudyRoomPort.queryTimeSlotById(timeSlotStub.id) } returns otherTimeSlotStub

        // when & then
        assertThrows<SchoolMismatchException> {
            updateTimeSlotUseCase.execute(timeSlotStub.id, newStartTime, newEndTime)
        }
    }
}
