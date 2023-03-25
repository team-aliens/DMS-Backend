package team.aliens.dms.domain.studyroom.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import team.aliens.dms.domain.file.spi.WriteFilePort
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.school.stub.createSchoolStub
import team.aliens.dms.domain.student.stub.createStudentStub
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQuerySchoolPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryStudentPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomQueryUserPort
import team.aliens.dms.domain.studyroom.spi.StudyRoomSecurityPort
import team.aliens.dms.domain.studyroom.spi.vo.StudentSeatApplicationVO
import team.aliens.dms.domain.studyroom.spi.vo.StudentSeatInfo
import team.aliens.dms.domain.studyroom.stub.createTimeSlotStub
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import team.aliens.dms.domain.user.stub.createUserStub
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
        createUserStub(
            id = managerId,
            schoolId = schoolId
        )
    }

    private val studentId = UUID.randomUUID()

    private val studentStub by lazy {
        createStudentStub(
            id = studentId
        )
    }

    private val schoolStub by lazy {
        createSchoolStub(
            id = schoolId
        )
    }

    private val timeSlotStub by lazy {
        createTimeSlotStub(
            schoolId = schoolId
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
