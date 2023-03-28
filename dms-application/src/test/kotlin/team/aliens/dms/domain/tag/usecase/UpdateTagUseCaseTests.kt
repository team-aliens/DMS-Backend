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

@ExtendWith(SpringExtension::class)
class UpdateTagUseCaseTests {

    private val securityPort: TagSecurityPort = mockk()
    private val queryUserPort: TagQueryUserPort = mockk()
    private val commandTagPort: CommandTagPort = mockk(relaxed = true)
    private val queryTagPort: QueryTagPort = mockk()

    private val updateTagUseCase = UpdateTagUseCase(
        securityPort, queryUserPort, commandTagPort, queryTagPort
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

    private val diffSchoolTagStub = createTagStub(
        id = tagId,
        name = "1OUT",
        schoolId = UUID.randomUUID()
    )

    @Test
    fun `태그 수정 성공`() {
        // given
        every { securityPort.getCurrentUserId() } returns currentUserId
        every { queryUserPort.queryUserById(currentUserId) } returns userStub
        every { queryTagPort.queryTagById(tagId) } returns tagStub
        every { queryTagPort.existsByNameAndSchoolId(tagStub.name, schoolId) } returns false

        // when & then
        assertDoesNotThrow {
            updateTagUseCase.execute(
                tagId = tagId,
                name = "Updated",
                color = "FFFFFFF"
            )
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
            updateTagUseCase.execute(
                tagId = tagId,
                name = "Updated",
                color = "FFFFFFF"
            )
        }
    }

    @Test
    fun `학교가 일치하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns currentUserId
        every { queryUserPort.queryUserById(currentUserId) } returns userStub
        every { queryTagPort.queryTagById(tagId) } returns diffSchoolTagStub

        // when & then
        assertThrows<SchoolMismatchException> {
            updateTagUseCase.execute(
                tagId = tagId,
                name = "Updated",
                color = "FFFFFFF"
            )
        }
    }

    @Test
    fun `유저가 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns currentUserId
        every { queryUserPort.queryUserById(currentUserId) } returns null

        // when & then
        assertThrows<UserNotFoundException> {
            updateTagUseCase.execute(
                tagId = tagId,
                name = "Updated",
                color = "FFFFFFF"
            )
        }
    }
}
