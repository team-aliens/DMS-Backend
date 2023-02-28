package team.aliens.dms.domain.remain.usecase

import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.file.spi.WriteFilePort
import team.aliens.dms.domain.remain.dto.RemainStatusInfo
import team.aliens.dms.domain.remain.dto.StudentRemainInfo
import team.aliens.dms.domain.remain.spi.QueryRemainStatusPort
import team.aliens.dms.domain.remain.spi.RemainQuerySchoolPort
import team.aliens.dms.domain.remain.spi.RemainQueryStudentPort
import team.aliens.dms.domain.remain.spi.RemainQueryUserPort
import team.aliens.dms.domain.remain.spi.RemainSecurityPort
import team.aliens.dms.domain.school.exception.SchoolNotFoundException
import team.aliens.dms.domain.school.model.School
import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import java.time.LocalDate
import java.util.UUID

@ExtendWith(SpringExtension::class)
class ExportRemainStatusUseCaseTests {

    private val securityPort: RemainSecurityPort = mockk(relaxed = true)
    private val queryUserPort: RemainQueryUserPort = mockk(relaxed = true)
    private val querySchoolPort: RemainQuerySchoolPort = mockk(relaxed = true)
    private val queryStudentPort: RemainQueryStudentPort = mockk(relaxed = true)
    private val queryRemainStatusPort: QueryRemainStatusPort = mockk(relaxed = true)
    private val writeFilePort: WriteFilePort = mockk(relaxed = true)

    private val exportRemainStatusUseCase = ExportRemainStatusUseCase(
        securityPort, queryUserPort, querySchoolPort, queryStudentPort, queryRemainStatusPort, writeFilePort
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

    private val remainStatusInfoStub by lazy {
        RemainStatusInfo(
            studentId = studentId,
            optionName = "잔류"
        )
    }

    @Test
    fun `잔류항목 생성 성공`() {
        // given
        every { securityPort.getCurrentUserId() } returns managerId
        every { queryUserPort.queryUserById(managerId) } returns userStub
        every { querySchoolPort.querySchoolById(schoolId) } returns schoolStub
        every { queryStudentPort.queryStudentsBySchoolId(schoolId) } returns listOf(studentStub)
        every { queryRemainStatusPort.queryByStudentIdIn(listOf(studentId)) } returns listOf(remainStatusInfoStub)

        val studentRemainInfoSlot = slot<List<StudentRemainInfo>>()
        every { writeFilePort.writeRemainStatusExcelFile(capture(studentRemainInfoSlot)) } returns byteArrayOf()

        // when
        val response = exportRemainStatusUseCase.execute()

        // then
        val studentRemainInfo = studentRemainInfoSlot.captured[0]

        assertAll(
            { assertEquals(studentRemainInfo.studentName, studentStub.name) },
            { assertEquals(studentRemainInfo.studentGcn, studentStub.gcn) },
            { assertEquals(studentRemainInfo.roomNumber, studentStub.roomNumber) },
            { assertEquals(studentRemainInfo.studentSex, studentStub.sex) },
            { assertEquals(studentRemainInfo.optionName, remainStatusInfoStub.optionName) },
            { assert(response.fileName.startsWith("${schoolStub.name.replace(" ", "")}_잔류_신청결과_")) }
        )
    }

    @Test
    fun `유저 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns managerId
        every { queryUserPort.queryUserById(managerId) } returns null

        // when & then
        assertThrows<UserNotFoundException> {
            exportRemainStatusUseCase.execute()
        }
    }

    @Test
    fun `학교 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns managerId
        every { queryUserPort.queryUserById(managerId) } returns userStub
        every { querySchoolPort.querySchoolById(schoolId) } returns null

        // when & then
        assertThrows<SchoolNotFoundException> {
            exportRemainStatusUseCase.execute()
        }
    }
}
