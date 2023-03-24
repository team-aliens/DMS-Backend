package team.aliens.dms.domain.studyroom.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.file.spi.WriteFilePort
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.studyroom.model.TimeSlot
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQuerySchoolPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryStudentPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.studyroom.spi.vo.StudentSeatApplicationVO
import team.aliens.dms.domain.studyroom.spi.vo.StudentSeatInfo
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import java.io.File
import java.time.LocalDate
import java.time.LocalTime
import java.util.UUID

class ExportStudyRoomStudentsApplicationStatusUseCaseTests {

    private val securityPort: StudyRoomSecurityPort = mockk(relaxed = true)
    private val queryUserPort: StudyRoomQueryUserPort = mockk(relaxed = true)
    private val querySchoolPort: StudyRoomQuerySchoolPort = mockk(relaxed = true)
    private val queryStudentPort: StudyRoomQueryStudentPort = mockk(relaxed = true)
    private val queryStudyRoomPort: QueryStudyRoomPort = mockk(relaxed = true)
    private val writeFilePort: WriteFilePort = mockk(relaxed = true)

    private val exportStudyRoomApplicationStatusUseCase = ExportStudyRoomApplicationStatusUseCase(
        securityPort, queryUserPort, querySchoolPort, queryStudentPort, queryStudyRoomPort, writeFilePort
    )

    private val managerId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()

    private val userStub by lazy {
        User(
            id = managerId,
            schoolId = schoolId,
            accountId = "accountId",
            password = "password",
            email = "email",
            authority = Authority.MANAGER,
            createdAt = null,
            deletedAt = null
        )
    }

    private val studentId = UUID.randomUUID()

    private val studentStub by lazy {
        Student(
            id = studentId,
            roomId = UUID.randomUUID(),
            roomNumber = "123",
            roomLocation = "A",
            schoolId = UUID.randomUUID(),
            grade = 1,
            classRoom = 1,
            number = 1,
            name = "김은빈",
            profileImageUrl = "https://~",
            sex = Sex.FEMALE
        )
    }

    private val schoolStub by lazy {
        School(
            id = schoolId,
            name = "대덕소프트웨어마이스터고등학교",
            code = "test code",
            question = "test question",
            answer = "test answer",
            address = "test address",
            contractStartedAt = LocalDate.now(),
            contractEndedAt = LocalDate.now(),
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

    private val timeSlotsStub by lazy {
        listOf(
            timeSlotStub,
            timeSlotStub.copy(id = UUID.randomUUID()),
            timeSlotStub.copy(id = UUID.randomUUID())
        )
    }

    private val studentSeatApplicationVOStub by lazy {
        StudentSeatApplicationVO(
            studentId = studentId,
            studyRoomName = "z",
            studyRoomFloor = 1,
            seatNumber = 1,
            seatTypeName = "z",
            timeSlotId = timeSlotStub.id
        )
    }

    @Test
    fun `자습실 신청상태 출력 성공 - baseFile 없는 경우`() {
        // given
        every { securityPort.getCurrentUserId() } returns managerId
        every { queryUserPort.queryUserById(managerId) } returns userStub
        every { queryStudentPort.queryStudentsBySchoolId(schoolId) } returns listOf(studentStub)
        every { queryStudyRoomPort.querySeatApplicationsByStudentIdIn(listOf(studentId)) } returns listOf(studentSeatApplicationVOStub)
        every { queryStudyRoomPort.queryTimeSlotsBySchoolId(schoolId) } returns timeSlotsStub
        every { querySchoolPort.querySchoolById(schoolId) } returns schoolStub

        val studentSeatInfoSlot = slot<List<StudentSeatInfo>>()
        every {
            writeFilePort.writeStudyRoomApplicationStatusExcelFile(
                timeSlots = timeSlotsStub,
                studentSeats = capture(studentSeatInfoSlot)
            )
        } returns byteArrayOf()

        // when
        val response = exportStudyRoomApplicationStatusUseCase.execute(null)

        // then
        val studentSeatInfo = studentSeatInfoSlot.captured[0]

        assertAll(
            { assertEquals(studentSeatInfo.studentName, studentStub.name) },
            { assertEquals(studentSeatInfo.studentGrade, studentStub.grade) },
            { assertEquals(studentSeatInfo.studentClassRoom, studentStub.classRoom) },
            { assertEquals(studentSeatInfo.studentNumber, studentStub.number) },
            { assertEquals(studentSeatInfo.timeSlotId, studentSeatApplicationVOStub.timeSlotId) },
            { assert(response.fileName.startsWith("${schoolStub.name.replace(" ", "")}_자습실_신청상태_")) }
        )
    }

    private val fileStub: File = mockk()

    @Test
    fun `자습실 신청상태 출력 성공 - baseFile 있는 경우`() {
        // given
        every { securityPort.getCurrentUserId() } returns managerId
        every { queryUserPort.queryUserById(managerId) } returns userStub
        every { queryStudentPort.queryStudentsBySchoolId(schoolId) } returns listOf(studentStub)
        every { queryStudyRoomPort.querySeatApplicationsByStudentIdIn(listOf(studentId)) } returns listOf(studentSeatApplicationVOStub)
        every { queryStudyRoomPort.queryTimeSlotsBySchoolId(schoolId) } returns timeSlotsStub
        every { querySchoolPort.querySchoolById(schoolId) } returns schoolStub

        val studentSeatInfoSlot = slot<Map<Pair<String, String>, StudentSeatInfo>>()
        every {
            writeFilePort.addStudyRoomApplicationStatusExcelFile(
                baseFile = fileStub,
                timeSlots = timeSlotsStub,
                studentSeatsMap = capture(studentSeatInfoSlot)
            )
        } returns byteArrayOf()

        // when
        val response = exportStudyRoomApplicationStatusUseCase.execute(fileStub)

        // then
        val studentSeatInfo = studentSeatInfoSlot.captured[Pair(studentStub.gcn, studentStub.name)]!!

        assertAll(
            { assertEquals(studentSeatInfo.studentName, studentStub.name) },
            { assertEquals(studentSeatInfo.studentGrade, studentStub.grade) },
            { assertEquals(studentSeatInfo.studentClassRoom, studentStub.classRoom) },
            { assertEquals(studentSeatInfo.studentNumber, studentStub.number) },
            { assertEquals(studentSeatInfo.timeSlotId, studentSeatApplicationVOStub.timeSlotId) },
            { assert(response.fileName.startsWith("${schoolStub.name.replace(" ", "")}_자습실_신청상태_")) }
        )
    }

    @Test
    fun `유저 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns managerId
        every { queryUserPort.queryUserById(managerId) } returns null

        // when & then
        assertThrows<UserNotFoundException> {
            exportStudyRoomApplicationStatusUseCase.execute(null)
        }
    }

    @Test
    fun `학교 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns managerId
        every { queryUserPort.queryUserById(managerId) } returns userStub
        every { queryStudentPort.queryStudentsBySchoolId(schoolId) } returns listOf(studentStub)
        every { queryStudyRoomPort.querySeatApplicationsByStudentIdIn(listOf(studentId)) } returns listOf(studentSeatApplicationVOStub)
        every { queryStudyRoomPort.queryTimeSlotsBySchoolId(schoolId) } returns timeSlotsStub
        every { querySchoolPort.querySchoolById(schoolId) } returns null

        // when & then
        assertThrows<SchoolNotFoundException> {
            exportStudyRoomApplicationStatusUseCase.execute(null)
        }
    }
}
