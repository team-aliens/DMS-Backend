package team.aliens.dms.domain.studyroom.usecase

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.studyroom.exception.StudyRoomNotFoundException
import team.aliens.dms.domain.studyroom.exception.StudyRoomTimeSlotNotFoundException
import team.aliens.dms.domain.studyroom.exception.TimeSlotNotFoundException
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.model.TimeSlot
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.stub.createUserStub
import java.time.LocalTime
import java.util.UUID

class StudentQueryStudyRoomUseCaseTests {

    private val securityPort: StudyRoomSecurityPort = mockk(relaxed = true)
    private val queryUserPort: StudyRoomQueryUserPort = mockk(relaxed = true)
    private val queryStudyRoomPort: QueryStudyRoomPort = mockk(relaxed = true)

    private val studentQueryRoomUseCase = StudentQueryStudyRoomUseCase(
        securityPort, queryUserPort, queryStudyRoomPort
    )

    private val userId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()
    private val studyRoomId = UUID.randomUUID()
    private val timeSlotId = UUID.randomUUID()

    private val userStub by lazy {
        createUserStub(
            id = userId,
            schoolId = schoolId
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

    private val timeSlotStub by lazy {
        TimeSlot(
            id = timeSlotId,
            schoolId = schoolId,
            startTime = LocalTime.of(0, 0),
            endTime = LocalTime.of(0, 0)
        )
    }

    @Test
    fun `자습실 조회 성공`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryStudyRoomPort.queryStudyRoomById(studyRoomId) } returns studyRoomStub
        every { queryStudyRoomPort.existsStudyRoomTimeSlotByStudyRoomIdAndTimeSlotId(studyRoomId, timeSlotId) } returns true
        every { queryStudyRoomPort.queryTimeSlotById(timeSlotId) } returns timeSlotStub

        // when & then
        assertDoesNotThrow {
            studentQueryRoomUseCase.execute(studyRoomId, timeSlotId)
        }
    }

    @Test
    fun `유저가 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns null

        // when & then
        assertThrows<UserNotFoundException> {
            studentQueryRoomUseCase.execute(studyRoomId, timeSlotId)
        }
    }

    @Test
    fun `자습실이 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryStudyRoomPort.queryStudyRoomById(studyRoomId) } returns null

        // when & then
        assertThrows<StudyRoomNotFoundException> {
            studentQueryRoomUseCase.execute(studyRoomId, timeSlotId)
        }
    }

    @Test
    fun `자습실에 대한 이용시간이 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryStudyRoomPort.queryStudyRoomById(studyRoomId) } returns studyRoomStub
        every { queryStudyRoomPort.existsStudyRoomTimeSlotByStudyRoomIdAndTimeSlotId(studyRoomId, timeSlotId) } returns false

        // when & then
        assertThrows<StudyRoomTimeSlotNotFoundException> {
            studentQueryRoomUseCase.execute(studyRoomId, timeSlotId)
        }
    }

    @Test
    fun `이용시간이 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryStudyRoomPort.queryStudyRoomById(studyRoomId) } returns studyRoomStub
        every { queryStudyRoomPort.existsStudyRoomTimeSlotByStudyRoomIdAndTimeSlotId(studyRoomId, timeSlotId) } returns true
        every { queryStudyRoomPort.queryTimeSlotById(timeSlotId) } returns null

        // when & then
        assertThrows<TimeSlotNotFoundException> {
            studentQueryRoomUseCase.execute(studyRoomId, timeSlotId)
        }
    }

    private val otherUserId = UUID.randomUUID()
    private val otherUserStub by lazy {
        createUserStub(
            id = otherUserId,
            schoolId = schoolId
        )
    }

    @Test
    fun `다른 학교의 자습실임`() {
        // given
        every { securityPort.getCurrentUserId() } returns otherUserId
        every { queryUserPort.queryUserById(userId) } returns otherUserStub
        every { queryStudyRoomPort.queryStudyRoomById(studyRoomId) } returns studyRoomStub

        // when & then
        assertThrows<SchoolMismatchException> {
            studentQueryRoomUseCase.execute(studyRoomId, timeSlotId)
        }
    }

    private val otherTimeSlotStub by lazy {
        TimeSlot(
            id = timeSlotId,
            schoolId = UUID.randomUUID(),
            startTime = LocalTime.of(0, 0),
            endTime = LocalTime.of(0, 0)
        )
    }

    @Test
    fun `다른 학교의 이용시간임`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryStudyRoomPort.queryStudyRoomById(studyRoomId) } returns studyRoomStub
        every { queryStudyRoomPort.existsStudyRoomTimeSlotByStudyRoomIdAndTimeSlotId(studyRoomId, timeSlotId) } returns true
        every { queryStudyRoomPort.queryTimeSlotById(timeSlotId) } returns otherTimeSlotStub
        every { queryStudyRoomPort.queryTimeSlotsBySchoolIdAndStudyRoomId(schoolId, studyRoomId) } returns listOf(otherTimeSlotStub)

        // when & then
        assertThrows<SchoolMismatchException> {
            studentQueryRoomUseCase.execute(studyRoomId, timeSlotId)
        }
    }
}
