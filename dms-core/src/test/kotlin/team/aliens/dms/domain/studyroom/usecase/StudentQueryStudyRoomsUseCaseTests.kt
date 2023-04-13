package team.aliens.dms.domain.studyroom.usecase

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.student.stub.createStudentStub
import team.aliens.dms.domain.studyroom.exception.TimeSlotNotFoundException
import team.aliens.dms.domain.studyroom.model.TimeSlot
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryStudentPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.studyroom.spi.vo.StudyRoomVO
import team.aliens.dms.domain.studyroom.stub.createTimeSlotStub
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.stub.createUserStub
import java.time.LocalTime
import java.util.UUID

class StudentQueryStudyRoomsUseCaseTests {

    private val securityPort: StudyRoomSecurityPort = mockk(relaxed = true)
    private val queryUserPort: StudyRoomQueryUserPort = mockk(relaxed = true)
    private val queryStudentPort: StudyRoomQueryStudentPort = mockk(relaxed = true)
    private val queryStudyRoomPort: QueryStudyRoomPort = mockk(relaxed = true)

    private val studentQueryRoomsUseCase = StudentQueryStudyRoomsUseCase(
        securityPort, queryUserPort, queryStudentPort, queryStudyRoomPort
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

    private val studentStub by lazy {
        createStudentStub(
            id = userId
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
        createTimeSlotStub(
            schoolId = schoolId
        )
    }

    @Test
    fun `자습실 목록 조회 성공`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryStudentPort.queryStudentById(userId) } returns studentStub
        every { queryStudyRoomPort.queryTimeSlotById(timeSlotId) } returns timeSlotStub
        every { queryStudyRoomPort.queryAllStudyRoomsByTimeSlotIdAndGradeAndSex(timeSlotId, studentStub.grade, studentStub.sex) } returns listOf(studyRoomStub)

        // when & then
        assertDoesNotThrow {
            studentQueryRoomsUseCase.execute(timeSlotId)
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

    @Test
    fun `학생이 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryStudentPort.queryStudentByUserId(userId) } returns null

        // when & then
        assertThrows<StudentNotFoundException> {
            studentQueryRoomsUseCase.execute(timeSlotId)
        }
    }

    @Test
    fun `이용시간이 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryStudyRoomPort.queryTimeSlotById(timeSlotId) } returns null

        // when & then
        assertThrows<TimeSlotNotFoundException> {
            studentQueryRoomsUseCase.execute(timeSlotId)
        }
    }
}
