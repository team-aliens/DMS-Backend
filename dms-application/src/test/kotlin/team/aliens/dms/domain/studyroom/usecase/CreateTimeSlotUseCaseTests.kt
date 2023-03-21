package team.aliens.dms.domain.studyroom.usecase

import io.mockk.every
import io.mockk.mockk
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.studyroom.exception.TimeSlotAlreadyExistsException
import team.aliens.dms.domain.studyroom.model.TimeSlot
import team.aliens.dms.domain.studyroom.spi.CommandStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User

class CreateTimeSlotUseCaseTests {

    private val securityPort: StudyRoomSecurityPort = mockk(relaxed = true)
    private val queryUserPort: StudyRoomQueryUserPort = mockk(relaxed = true)
    private val queryStudyRoomPort: QueryStudyRoomPort = mockk(relaxed = true)
    private val commandStudyRoomPort: CommandStudyRoomPort = mockk(relaxed = true)

    private val createTimeSlotUseCase = CreateTimeSlotUseCase(
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

    @Test
    fun `이용시간 생성 성공`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { commandStudyRoomPort.saveTimeSlot(any()) } returns timeSlotStub
        every { queryStudyRoomPort.existsTimeSlotByStartTimeAndEndTime(timeSlotStub.startTime, timeSlotStub.endTime) } returns false

        // when
        val response = createTimeSlotUseCase.execute(timeSlotStub.startTime, timeSlotStub.endTime)

        // then
        assertAll(
            { assertEquals(response, timeSlotStub.id) },
        )
    }

    @Test
    fun `이용시간 생성 성공 - TimeSlot이 존재하지 않았던 경우`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { commandStudyRoomPort.saveTimeSlot(any()) } returns timeSlotStub
        every { queryStudyRoomPort.existsTimeSlotByStartTimeAndEndTime(timeSlotStub.startTime, timeSlotStub.endTime) } returns false

        // when
        val response = createTimeSlotUseCase.execute(timeSlotStub.startTime, timeSlotStub.endTime)

        // then
        assertAll(
            { assertEquals(response, timeSlotStub.id) },
        )
    }

    @Test
    fun `유저가 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns null

        // when & then
        assertThrows<UserNotFoundException> {
            createTimeSlotUseCase.execute(timeSlotStub.startTime, timeSlotStub.endTime)
        }
    }

    @Test
    fun `이미 존재하는 시간대임`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryStudyRoomPort.existsTimeSlotByStartTimeAndEndTime(timeSlotStub.startTime, timeSlotStub.endTime) } returns true

        // when & then
        assertThrows<TimeSlotAlreadyExistsException> {
            createTimeSlotUseCase.execute(timeSlotStub.startTime, timeSlotStub.endTime)
        }
    }
}
