package team.aliens.dms.domain.point.usecase

import io.mockk.every
import io.mockk.mockk
import java.util.UUID
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.point.dto.UpdatePointOptionRequest
import team.aliens.dms.domain.point.exception.PointOptionNotFoundException
import team.aliens.dms.domain.point.model.PointOption
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.CommandPointOptionPort
import team.aliens.dms.domain.point.spi.PointQueryUserPort
import team.aliens.dms.domain.point.spi.PointSecurityPort
import team.aliens.dms.domain.point.spi.QueryPointOptionPort
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User

@ExtendWith(SpringExtension::class)
class UpdatePointOptionUseCaseTest {

    private val securityPort: PointSecurityPort = mockk()
    private val queryUserPort: PointQueryUserPort = mockk()
    private val queryPointOptionPort: QueryPointOptionPort = mockk()
    private val commandPointOptionPort: CommandPointOptionPort = mockk(relaxed = true)

    private val updatePointOptionUseCase: UpdatePointOptionUseCase = UpdatePointOptionUseCase(
        securityPort, queryUserPort, queryPointOptionPort, commandPointOptionPort
    )

    private val currentUserId = UUID.randomUUID()
    private val managerId = currentUserId
    private val pointOptionId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()
    private val schoolId2 = UUID.randomUUID()

    private val userStub by lazy {
            User(
                id = managerId,
                schoolId = schoolId,
                accountId = "manager",
                password = "pw",
                email = "manaager@manager.com",
                authority = Authority.MANAGER,
                createdAt = null,
                deletedAt = null
            )
    }

    private val requestStub by lazy {
        UpdatePointOptionRequest(
            name = "updated",
            score = 20,
            type = "BONUS"
        )
    }

    private val pointOptionStub by lazy {
        PointOption(
            name = "before",
            score = 10,
            type = PointType.MINUS,
            schoolId = schoolId
        )
    }

    private val otherPointOptionStub by lazy {
        PointOption(
            name = "before",
            score = 10,
            type = PointType.MINUS,
            schoolId = schoolId2
        )
    }

    @Test
    fun `상벌점 항목 변경 성공`() {
        // given
        every { securityPort.getCurrentUserId() } returns currentUserId
        every { queryUserPort.queryUserById(currentUserId) } returns userStub
        every { queryPointOptionPort.queryPointOptionById(pointOptionId) } returns pointOptionStub

        // when & then
        assertDoesNotThrow {
            updatePointOptionUseCase.execute(requestStub, pointOptionId)
        }
    }

    @Test
    fun `상벌점 항목을 찾을 수 없음`() {
        // given
        every { securityPort.getCurrentUserId() } returns currentUserId
        every { queryUserPort.queryUserById(currentUserId) } returns userStub
        every { queryPointOptionPort.queryPointOptionById(pointOptionId) } returns null

        // when & then
        assertThrows<PointOptionNotFoundException> {
            updatePointOptionUseCase.execute(requestStub, pointOptionId)
        }
    }

    @Test
    fun `다른 학교 상벌점일 경우`() {
        // given
        every { securityPort.getCurrentUserId() } returns currentUserId
        every { queryUserPort.queryUserById(currentUserId) } returns userStub
        every { queryPointOptionPort.queryPointOptionById(pointOptionId) } returns otherPointOptionStub

        // when & then
        assertThrows<SchoolMismatchException> {
            updatePointOptionUseCase.execute(requestStub, pointOptionId)
        }
    }

    @Test
    fun `유저를 찾을 수 없음`() {
        // given
        every { securityPort.getCurrentUserId() } returns currentUserId
        every { queryUserPort.queryUserById(currentUserId) } returns null

        // when & then
        assertThrows<UserNotFoundException> {
            updatePointOptionUseCase.execute(requestStub, pointOptionId)
        }
    }
}