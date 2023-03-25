package team.aliens.dms.domain.tag.usecase

import io.mockk.every
import io.mockk.mockk
import java.util.UUID
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


@ExtendWith(SpringExtension::class)
class RemoveTagUseCaseTest {

    private val securityPort: TagSecurityPort = mockk()
    private val queryUserPort: TagQueryUserPort = mockk()
    private val queryTagPort: QueryTagPort = mockk()
    private val commandTagPort: CommandTagPort = mockk(relaxed = true)

    private val removeTagUseCase = RemoveTagUseCase(
        securityPort, queryUserPort, queryTagPort, commandTagPort
    )

    private val currentUserId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()
    private val tagId = UUID.randomUUID()

    private val userStub = createUserStub(
        currentUserId,
        schoolId
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
    fun `삭제 권한이 없음`() {
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
    fun `사용자를 찾을 수 없음`() {
        // given
        every { securityPort.getCurrentUserId() } returns currentUserId
        every { queryUserPort.queryUserById(currentUserId) } returns null

        // when & then
        assertThrows<UserNotFoundException> {
            removeTagUseCase.execute(tagId)
        }
    }
}