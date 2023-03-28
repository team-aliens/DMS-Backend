package team.aliens.dms.domain.tag.usecase

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.tag.exception.TagNotFoundException
import team.aliens.dms.domain.tag.spi.CommandTagPort
import team.aliens.dms.domain.tag.spi.QueryTagPort
import team.aliens.dms.domain.tag.spi.TagQueryUserPort
import team.aliens.dms.domain.tag.spi.TagSecurityPort
import team.aliens.dms.domain.tag.stub.createTagStub
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.stub.createUserStub
import java.util.UUID
<<<<<<< develop:dms-application/src/test/kotlin/team/aliens/dms/domain/tag/usecase/RemoveTagUseCaseTest.kt
import team.aliens.dms.domain.tag.spi.CommandStudentTagPort
=======
>>>>>>> style: (#394) 테스트 이름 변경:dms-application/src/test/kotlin/team/aliens/dms/domain/tag/usecase/RemoveTagUseCaseTests.kt

@ExtendWith(SpringExtension::class)
class RemoveTagUseCaseTests {

    private val securityPort: TagSecurityPort = mockk()
    private val queryUserPort: TagQueryUserPort = mockk()
    private val queryTagPort: QueryTagPort = mockk()
    private val commandTagPort: CommandTagPort = mockk(relaxed = true)
    private val commandStudentTagPort: CommandStudentTagPort = mockk(relaxed = true)

    private val removeTagUseCase = RemoveTagUseCase(
        securityPort, queryUserPort, queryTagPort, commandTagPort, commandStudentTagPort
    )

    private val currentUserId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()
    private val tagId = UUID.randomUUID()

    private val userStub = createUserStub(
        id = currentUserId,
        schoolId = schoolId
    )

    private val tagStub = createTagStub(
        id = tagId,
        name = "1OUT",
        schoolId = schoolId
    )

    @Test
    fun `태그 삭제 성공`() {
        // given
        every { securityPort.getCurrentUserId() } returns currentUserId
        every { queryUserPort.queryUserById(currentUserId) } returns userStub
        every { queryTagPort.queryTagById(tagId) } returns tagStub

        // when & then
        assertDoesNotThrow {
            removeTagUseCase.execute(tagId)
        }
    }

    @Test
    fun `태그가 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns currentUserId
        every { queryUserPort.queryUserById(currentUserId) } returns userStub
        every { queryTagPort.queryTagById(tagId) } returns null

        // when & then
        assertThrows<TagNotFoundException> {
            removeTagUseCase.execute(tagId)
        }
    }

    @Test
    fun `학교가 일치하지 않음`() {
        // given
        val diffSchoolTag = createTagStub(
            id = tagId,
            name = "1OUT"
        )
        every { securityPort.getCurrentUserId() } returns currentUserId
        every { queryUserPort.queryUserById(currentUserId) } returns userStub
        every { queryTagPort.queryTagById(tagId) } returns diffSchoolTag

        // when & then
        assertThrows<SchoolMismatchException> {
            removeTagUseCase.execute(tagId)
        }
    }

    @Test
    fun `유저가 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns currentUserId
        every { queryUserPort.queryUserById(currentUserId) } returns null

        // when & then
        assertThrows<UserNotFoundException> {
            removeTagUseCase.execute(tagId)
        }
    }
}
