package team.aliens.dms.domain.tag.usecase

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.student.exception.StudentNotFoundException
import team.aliens.dms.domain.student.stub.createStudentStub
import team.aliens.dms.domain.tag.exception.TagNotFoundException
import team.aliens.dms.domain.tag.spi.CommandTagPort
import team.aliens.dms.domain.tag.spi.QueryTagPort
import team.aliens.dms.domain.tag.spi.TagQueryStudentPort
import team.aliens.dms.domain.tag.spi.TagQueryUserPort
import team.aliens.dms.domain.tag.spi.TagSecurityPort
import team.aliens.dms.domain.tag.stub.createTagStub
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.stub.createUserStub
import java.util.UUID

class CancelGrantedTagUseCaseTests {

    private val securityPort: TagSecurityPort = mockk(relaxed = true)
    private val queryUserPort: TagQueryUserPort = mockk(relaxed = true)
    private val queryTagPort: QueryTagPort = mockk(relaxed = true)
    private val queryStudentPort: TagQueryStudentPort = mockk(relaxed = true)
    private val commandTagPort: CommandTagPort = mockk(relaxed = true)

    private val cancelGrantedTagUseCase = CancelGrantedTagUseCase(
        securityPort, queryUserPort, queryTagPort, queryStudentPort, commandTagPort
    )

    private val userId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()
    private val tagId = UUID.randomUUID()
    private val studentId = UUID.randomUUID()

    private val userStub by lazy {
        createUserStub(
            id = userId,
            schoolId = schoolId,
            authority = Authority.MANAGER
        )
    }

    private val tagStub by lazy {
        createTagStub(
            id = tagId,
            schoolId = schoolId
        )
    }

    private val otherSchoolTagStub by lazy {
        createTagStub()
    }

    private val studentStub by lazy {
        createStudentStub(
            id = studentId,
            schoolId = schoolId
        )
    }

    @Test
    fun `학생 태그 부여 취소 성공`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryTagPort.queryTagById(tagId) } returns tagStub
        every { queryStudentPort.queryStudentById(studentId) } returns studentStub

        // when & then
        assertDoesNotThrow {
            cancelGrantedTagUseCase.execute(studentId, tagId)
        }
    }

    @Test
    fun `관리자를 찾지 못함`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns null

        // when & then
        assertThrows<UserNotFoundException> {
            cancelGrantedTagUseCase.execute(studentId, tagId)
        }
    }

    @Test
    fun `태그를 찾지 못함`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryTagPort.queryTagById(tagId) } returns null

        // when & then
        assertThrows<TagNotFoundException> {
            cancelGrantedTagUseCase.execute(studentId, tagId)
        }
    }

    @Test
    fun `매니저와 태그의 학교 식별자가 다름`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryTagPort.queryTagById(tagId) } returns otherSchoolTagStub

        // when & then
        assertThrows<SchoolMismatchException> {
            cancelGrantedTagUseCase.execute(studentId, tagId)
        }
    }

    @Test
    fun `학생을 찾지 못함`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryTagPort.queryTagById(tagId) } returns tagStub
        every { queryStudentPort.queryStudentById(studentId) } returns null

        // when & then
        assertThrows<StudentNotFoundException> {
            cancelGrantedTagUseCase.execute(studentId, tagId)
        }
    }
}