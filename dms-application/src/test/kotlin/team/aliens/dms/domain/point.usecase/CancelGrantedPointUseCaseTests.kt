package team.aliens.dms.domain.point.usecase

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.point.exception.PointHistoryNotFoundException
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.CommandPointHistoryPort
import team.aliens.dms.domain.point.spi.PointQueryUserPort
import team.aliens.dms.domain.point.spi.PointSecurityPort
import team.aliens.dms.domain.point.spi.QueryPointHistoryPort
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import java.time.LocalDateTime
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

    private val pointHistoryStub by lazy {
        PointHistory(
            studentName = "김은빈",
            studentGcn = "2106",
            bonusTotal = 3,
            minusTotal = 0,
            isCancel = false,
            pointName = "분리수거",
            pointScore = 3,
            pointType = PointType.BONUS,
            createdAt = LocalDateTime.of(2023, 3, 5, 12, 0),
            schoolId = schoolId
        )
    }

    @Test
    fun `상벌점 부여 취소 성공`() {
        // given
        every { securityPort.getCurrentUserId() } returns managerId

        every { queryUserPort.queryUserById(managerId) } returns userStub

        every { queryPointHistoryPort.queryPointHistoryById(pointHistoryId) } returns pointHistoryStub

        // when & then
        assertDoesNotThrow {
            cancelGrantedPointUseCase.execute(pointHistoryId)
        }
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
