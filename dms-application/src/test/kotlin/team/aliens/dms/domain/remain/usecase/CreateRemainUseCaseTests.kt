package team.aliens.dms.domain.remain.usecase

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.remain.model.RemainOption
import team.aliens.dms.domain.remain.spi.CommandRemainOptionPort
import team.aliens.dms.domain.remain.spi.RemainSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import team.aliens.dms.domain.user.spi.QueryUserPort
import java.util.UUID

@ExtendWith(SpringExtension::class)
class CreateRemainUseCaseTests {

    private val securityPort: RemainSecurityPort = mockk(relaxed = true)
    private val queryUserPort: QueryUserPort = mockk(relaxed = true)
    private val commentRemainOptionPort: CommandRemainOptionPort = mockk(relaxed = true)

    private val createRemainOptionUseCase = CreateRemainOptionUseCase(securityPort, queryUserPort, commentRemainOptionPort)

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
    private val description = "description"

    private val remainOptionStub by lazy {
        RemainOption(
            id = remainOptionId,
            schoolId = schoolId,
            title = title,
            description = description
        )
    }

    @Test
    fun `잔류항목 생성 성공`() {
        // given
        every { securityPort.getCurrentUserId() } returns managerId
        every { queryUserPort.queryUserById(managerId) } returns userStub
        every { commentRemainOptionPort.saveRemainOption(any()) } returns remainOptionStub

        // when
        val response = createRemainOptionUseCase.execute(title, description)

        // then
        assertEquals(response, remainOptionStub.id)
    }

    @Test
    fun `유저 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns managerId
        every { queryUserPort.queryUserById(managerId) } returns null

        // when & then
        assertThrows<UserNotFoundException> {
            createRemainOptionUseCase.execute(title, description)
        }
    }

}