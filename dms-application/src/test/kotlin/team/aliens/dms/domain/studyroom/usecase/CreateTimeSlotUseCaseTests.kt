package team.aliens.dms.domain.studyroom.usecase

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.model.TimeSlot
import team.aliens.dms.domain.studyroom.spi.CommandStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

class CreateTimeSlotUseCaseTests {

    private val securityPort: StudyRoomSecurityPort = mockk(relaxed = true)
    private val queryUserPort: StudyRoomQueryUserPort = mockk(relaxed = true)
    private val commandStudyRoomPort: CommandStudyRoomPort = mockk(relaxed = true)

    private val createTimeSlotUseCase = CreateTimeSlotUseCase(
        securityPort, queryUserPort, commandStudyRoomPort
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

    private val studyRoomInfosStubs by lazy {
        listOf(
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
            ),
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
}
