package team.aliens.dms.domain.studyroom.usecase

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.studyroom.model.StudyRoomTimeSlot
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.studyroom.spi.vo.StudyRoomVO
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

class StudentQueryStudyRoomsUseCaseTests {

    private val securityPort: StudyRoomSecurityPort = mockk(relaxed = true)
    private val queryUserPort: StudyRoomQueryUserPort = mockk(relaxed = true)
    private val queryStudyRoomPort: QueryStudyRoomPort = mockk(relaxed = true)

    private val studentQueryRoomsUseCase = StudentQueryStudyRoomsUseCase(
        securityPort, queryUserPort, queryStudyRoomPort
    )

    private val userId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()
    private val timeSlotId = UUID.randomUUID()

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

    private val studyRoomStub by lazy {
        StudyRoomVO(
            id = UUID.randomUUID(),
            floor = 1,
            name = "",
            availableGrade = 0,
            availableSex = Sex.FEMALE,
            inUseHeadcount = 3,
            totalAvailableSeat = 1,
        )
    }

    private val timeSlotStub by lazy {
        StudyRoomTimeSlot(
            id = UUID.randomUUID(),
            schoolId = schoolId,
            startTime = LocalTime.of(0, 0),
            endTime = LocalTime.of(0, 0)
        )
    }

    @Test
    fun `자습실 목록 조회 성공`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryStudyRoomPort.queryTimeSlotById(timeSlotId) } returns timeSlotStub
        every { queryStudyRoomPort.queryAllStudyRoomsByTimeSlotId(timeSlotId) } returns listOf(studyRoomStub)

        // when & then
        assertDoesNotThrow {
            studentQueryRoomsUseCase.execute(timeSlotId)
        }
    }

    private val otherTimeSlotStub by lazy {
        StudyRoomTimeSlot(
            id = UUID.randomUUID(),
            schoolId = UUID.randomUUID(),
            startTime = LocalTime.of(0, 0),
            endTime = LocalTime.of(0, 0)
        )
    }

    @Test
    fun `학교 불일치`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryStudyRoomPort.queryTimeSlotById(timeSlotId) } returns otherTimeSlotStub

        // when & then
        assertThrows<SchoolMismatchException> {
            studentQueryRoomsUseCase.execute(timeSlotId)
        }
    }

    @Test
    fun `유저가 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns null

        // when & then
        assertThrows<UserNotFoundException> {
            studentQueryRoomsUseCase.execute(timeSlotId)
        }
    }
}
