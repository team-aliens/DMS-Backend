package team.aliens.dms.domain.remain.usecase

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.remain.exception.RemainOptionNotFoundException
import team.aliens.dms.domain.remain.model.RemainOption
import team.aliens.dms.domain.remain.spi.CommandRemainOptionPort
import team.aliens.dms.domain.remain.spi.CommandRemainStatusPort
import team.aliens.dms.domain.remain.spi.QueryRemainOptionPort
import team.aliens.dms.domain.remain.spi.RemainQueryUserPort
import team.aliens.dms.domain.remain.spi.RemainSecurityPort
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import java.util.UUID

@ExtendWith(SpringExtension::class)
class RemoveRemainOptionUseCaseTests {

    private val securityPort: RemainSecurityPort = mockk(relaxed = true)
    private val queryUserPort: RemainQueryUserPort = mockk(relaxed = true)
    private val queryRemainOptionPort: QueryRemainOptionPort = mockk(relaxed = true)
    private val commandRemainOptionPort: CommandRemainOptionPort = mockk(relaxed = true)
    private val commandRemainStatusPort: CommandRemainStatusPort = mockk(relaxed = true)

    private val removeRemainOptionUseCase = RemoveRemainOptionUseCase(
        securityPort, queryUserPort, queryRemainOptionPort, commandRemainOptionPort, commandRemainStatusPort
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

    private val remainOptionId = UUID.randomUUID()
    private val title = "title"
    private val description = "descripton"

    private val remainOptionStub by lazy {
        RemainOption(
            id = remainOptionId,
            schoolId = schoolId,
            title = title,
            description = description
        )
    }

    @Test
    fun `잔류항목 삭제 성공`() {
        // given
        every { securityPort.getCurrentUserId() } returns managerId

        every { queryUserPort.queryUserById(managerId) } returns userStub

        every { queryRemainOptionPort.queryRemainOptionById(remainOptionId) } returns remainOptionStub

        // when & then
        assertDoesNotThrow {
            removeRemainOptionUseCase.execute(remainOptionId)
        }
    }

    @Test
    fun `잔류항목이 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns managerId

        every { queryUserPort.queryUserById(managerId) } returns userStub

        every { queryRemainOptionPort.queryRemainOptionById(remainOptionId) } returns null

        // when & then
        assertThrows<RemainOptionNotFoundException> {
            removeRemainOptionUseCase.execute(remainOptionId)
        }
    }

    private val otherRemainOptionStub by lazy {
        RemainOption(
            id = remainOptionId,
            schoolId = UUID.randomUUID(),
            title = title,
            description = description
        )
    }

    @Test
    fun `같은 학교의 매니저가 아님`() {
        // given
        every { securityPort.getCurrentUserId() } returns managerId

        every { queryUserPort.queryUserById(managerId) } returns userStub

        every { queryRemainOptionPort.queryRemainOptionById(remainOptionId) } returns otherRemainOptionStub

        // when & then
        assertThrows<SchoolMismatchException> {
            removeRemainOptionUseCase.execute(remainOptionId)
        }
    }

    @Test
    fun `유저가 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns managerId

        every { queryUserPort.queryUserById(managerId) } returns null

        // when & then
        assertThrows<UserNotFoundException> {
            removeRemainOptionUseCase.execute(remainOptionId)
        }
    }
}
