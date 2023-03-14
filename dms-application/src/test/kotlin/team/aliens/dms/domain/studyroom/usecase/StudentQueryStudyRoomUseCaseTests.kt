package team.aliens.dms.domain.studyroom.usecase

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.studyroom.StudyRoomFacade
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.user.model.User
import java.time.LocalDateTime
import java.util.UUID

class StudentQueryStudyRoomUseCaseTests {

    private val securityPort: StudyRoomSecurityPort = mockk(relaxed = true)
    private val queryUserPort: StudyRoomQueryUserPort = mockk(relaxed = true)
    private val queryStudyRoomPort: QueryStudyRoomPort = mockk(relaxed = true)
    private val studyRoomFacade: StudyRoomFacade = mockk(relaxed = true)

    private val studentQueryRoomUseCase = StudentQueryStudyRoomUseCase(
        securityPort, queryUserPort, queryStudyRoomPort, studyRoomFacade
    )

    private val userId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()
    private val studyRoomId = UUID.randomUUID()
    private val timeSlotId = UUID.randomUUID()

    private val userStub by lazy {
        User(
            id = userId,
            schoolId = schoolId,
            accountId = "test account id",
            password = "password",
            email = "test email",
            authority = Authority.STUDENT,
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )
    }

    private val studyRoomStub by lazy {
        StudyRoom(
            id = studyRoomId,
            schoolId = schoolId,
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
    fun `자습실 조회 성공`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryStudyRoomPort.queryStudyRoomById(studyRoomId) } returns studyRoomStub
        every { queryStudyRoomPort.existsTimeSlotById(timeSlotId) } returns true

        // when & then
        assertDoesNotThrow {
            studentQueryRoomUseCase.execute(studyRoomId, timeSlotId)
        }
    }

    private val otherUserId = UUID.randomUUID()
    private val otherUserStub by lazy {
        User(
            id = otherUserId,
            schoolId = schoolId,
            accountId = "test account id",
            password = "password",
            email = "test email",
            authority = Authority.STUDENT,
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )
    }

    @Test
    fun `다른 학교의 자습실임`() {
        // given
        every { securityPort.getCurrentUserId() } returns otherUserId
        every { queryUserPort.queryUserById(userId) } returns otherUserStub
        every { queryStudyRoomPort.queryStudyRoomById(studyRoomId) } returns studyRoomStub
        every { queryStudyRoomPort.queryStudyRoomById(studyRoomStub.id) } returns studyRoomStub

        // when & then
        assertThrows<SchoolMismatchException> {
            studentQueryRoomUseCase.execute(studyRoomId, timeSlotId)
        }
    }
}
