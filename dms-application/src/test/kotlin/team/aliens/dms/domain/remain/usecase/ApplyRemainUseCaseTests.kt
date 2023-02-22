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
import team.aliens.dms.domain.remain.spi.CommandRemainStatusPort
import team.aliens.dms.domain.remain.spi.QueryRemainOptionPort
import team.aliens.dms.domain.remain.spi.RemainQueryUserPort
import team.aliens.dms.domain.remain.spi.RemainSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import java.util.UUID

@ExtendWith(SpringExtension::class)
class ApplyRemainUseCaseTests {

    private val securityPort: RemainSecurityPort = mockk(relaxed = true)
    private val queryUserPort: RemainQueryUserPort = mockk(relaxed = true)
    private val queryRemainOptionPort: QueryRemainOptionPort = mockk(relaxed = true)
    private val commandRemainStatusPort: CommandRemainStatusPort = mockk(relaxed = true)

    private val applyRemainUseCase = ApplyRemainUseCase(
        securityPort, queryUserPort, queryRemainOptionPort, commandRemainStatusPort
    )

    private val userId = UUID.randomUUID()

    private val userStub by lazy {
        User(
            id = userId,
            schoolId = UUID.randomUUID(),
            accountId = "accountId",
            password = "password",
            email = "email",
            authority = Authority.MANAGER,
            createdAt = null,
            deletedAt = null
        )
    }

    private val remainOptionId = UUID.randomUUID()

    private val remainOptionStub by lazy {
        RemainOption(
            id = remainOptionId,
            schoolId = UUID.randomUUID(),
            title = "",
            description = ""
        )
    }

    @Test
    fun `잔류 신청 성공`() {
        //givenr
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryRemainOptionPort.queryRemainOptionById(remainOptionId) } returns remainOptionStub

        // when & then
        assertDoesNotThrow {
            applyRemainUseCase.execute(remainOptionId)
        }
    }

    @Test
    fun `유저가 존재하지 않음`() {
        //given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns null

        // when & then
        assertThrows<UserNotFoundException> {
            applyRemainUseCase.execute(remainOptionId)
        }
    }

    @Test
    fun `잔류 항목이 존재하지 않음`() {
        //given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryRemainOptionPort.queryRemainOptionById(remainOptionId) } returns null

        // when & then
        assertThrows<RemainOptionNotFoundException> {
            applyRemainUseCase.execute(remainOptionId)
        }
    }
}