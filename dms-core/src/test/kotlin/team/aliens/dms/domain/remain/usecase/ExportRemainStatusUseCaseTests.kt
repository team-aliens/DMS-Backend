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
import team.aliens.dms.domain.school.stub.createSchoolStub
import team.aliens.dms.domain.student.stub.createStudentStub
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.stub.createUserStub
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
        createUserStub(
            id = managerId,
            schoolId = schoolId,
            authority = Authority.MANAGER
        )
    }

    private val schoolStub by lazy {
        createSchoolStub(id = schoolId)
    }

    private val studentId = UUID.randomUUID()

    private val studentStub by lazy {
        createStudentStub(id = studentId)
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
