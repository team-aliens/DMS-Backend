package team.aliens.dms.domain.tag.usecase

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.tag.exception.TagAlreadyExistsException
import team.aliens.dms.domain.tag.spi.CommandTagPort
import team.aliens.dms.domain.tag.spi.QueryTagPort
import team.aliens.dms.domain.tag.spi.TagQueryUserPort
import team.aliens.dms.domain.tag.spi.TagSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.stub.createUserStub
import java.util.UUID

@ExtendWith(SpringExtension::class)
class CreateTagUseCaseTests {

    private val securityPort: TagSecurityPort = mockk()
    private val queryUserPort: TagQueryUserPort = mockk()
    private val commandTagPort: CommandTagPort = mockk(relaxed = true)
    private val queryTagPort: QueryTagPort = mockk()

    private val createTagUseCase = CreateTagUseCase(
        securityPort, queryUserPort, commandTagPort, queryTagPort
    )

    private val currentUserId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()
    private val name = "소프트웨어개발과"
    private val color = "FFFFFFF"

    private val userStub = createUserStub(
        id = currentUserId,
        schoolId = schoolId
    )

    @Test
    fun `태그 생성 성공`() {
        // given
        every { securityPort.getCurrentUserId() } returns currentUserId
        every { queryUserPort.queryUserById(currentUserId) } returns userStub
        every { queryTagPort.existsByNameAndSchoolId(name, schoolId) } returns false

        // when & then
        assertDoesNotThrow {
            createTagUseCase.execute(name, color)
        }
    }

    @Test
    fun `태그가 존재함`() {
        // given
        every { securityPort.getCurrentUserId() } returns currentUserId
        every { queryUserPort.queryUserById(currentUserId) } returns userStub
        every { queryTagPort.existsByNameAndSchoolId(name, schoolId) } returns true

        // when & then
        assertThrows<TagAlreadyExistsException> {
            createTagUseCase.execute(name, color)
        }
    }

    @Test
    fun `사용자를 찾을 수 없음`() {
        // given
        every { securityPort.getCurrentUserId() } returns currentUserId
        every { queryUserPort.queryUserById(currentUserId) } returns null

        // when & then
        assertThrows<UserNotFoundException> {
            createTagUseCase.execute(name, color)
        }
    }
}
