package team.aliens.dms.domain.point.usecase

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.point.exception.PointHistoryNotFoundException
import team.aliens.dms.domain.point.spi.CommandPointHistoryPort
import team.aliens.dms.domain.point.spi.PointQueryUserPort
import team.aliens.dms.domain.point.spi.PointSecurityPort
import team.aliens.dms.domain.point.spi.QueryPointHistoryPort
import team.aliens.dms.domain.point.stub.createPointHistoryStub
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import team.aliens.dms.domain.user.stub.createUserStub
import java.util.UUID

@ExtendWith(SpringExtension::class)
class CancelGrantedPointUseCaseTests {

    private val commandPointHistoryPort: CommandPointHistoryPort = mockk(relaxed = true)
    private val queryPointHistoryPort: QueryPointHistoryPort = mockk(relaxed = true)
    private val securityPort: PointSecurityPort = mockk(relaxed = true)
    private val queryUserPort: PointQueryUserPort = mockk(relaxed = true)

    private val cancelGrantedPointUseCase = CancelGrantedPointUseCase(
        commandPointHistoryPort, queryPointHistoryPort, securityPort, queryUserPort
    )

    private val pointHistoryId = UUID.randomUUID()

    private val managerId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()

    private val userStub by lazy {
        createUserStub(
            id = managerId,
            schoolId = schoolId,
            authority = Authority.MANAGER
        )
    }

    private val pointHistoryStub by lazy {
        createPointHistoryStub(schoolId = schoolId)
    }

    @Test
    fun `유저가 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns managerId

        every { queryUserPort.queryUserById(managerId) } returns null

        // when & then
        assertThrows<UserNotFoundException> {
            cancelGrantedPointUseCase.execute(pointHistoryId)
        }
    }

    @Test
    fun `pointHistory 미존재`() {
        // given
        every { securityPort.getCurrentUserId() } returns managerId

        every { queryUserPort.queryUserById(managerId) } returns userStub

        every { queryPointHistoryPort.queryPointHistoryById(pointHistoryId) } returns null

        // when & then
        assertThrows<PointHistoryNotFoundException> {
            cancelGrantedPointUseCase.execute(pointHistoryId)
        }
    }

    private val otherUserStub by lazy {
        User(
            id = managerId,
            schoolId = UUID.randomUUID(),
            accountId = "accountId",
            password = "password",
            email = "email",
            authority = Authority.MANAGER,
            createdAt = null,
            deletedAt = null
        )
    }

    @Test
    fun `학교 불일치`() {
        // given
        every { securityPort.getCurrentUserId() } returns managerId

        every { queryUserPort.queryUserById(managerId) } returns otherUserStub

        every { queryPointHistoryPort.queryPointHistoryById(pointHistoryId) } returns pointHistoryStub

        // when & then
        assertThrows<SchoolMismatchException> {
            cancelGrantedPointUseCase.execute(pointHistoryId)
        }
    }
}
