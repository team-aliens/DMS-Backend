package team.aliens.dms.domain.point.usecase

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.point.exception.PointOptionNotFoundException
import team.aliens.dms.domain.point.spi.CommandPointOptionPort
import team.aliens.dms.domain.point.spi.PointQueryUserPort
import team.aliens.dms.domain.point.spi.PointSecurityPort
import team.aliens.dms.domain.point.spi.QueryPointOptionPort
import team.aliens.dms.domain.point.stub.createPointOptionStub
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.stub.createUserStub
import java.util.UUID

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
        createUserStub(
            id = managerId, schoolId = schoolId, authority = Authority.MANAGER)

    }

    private val pointOptionStub by lazy {
        createPointOptionStub(schoolId = schoolId)
    }

    private val otherPointOptionStub by lazy {
        createPointOptionStub(schoolId = schoolId2)
    }

    @Test
    fun `상벌점 항목 변경 성공`() {
        // given
        every { securityPort.getCurrentUserId() } returns currentUserId
        every { queryUserPort.queryUserById(currentUserId) } returns userStub
        every { queryPointOptionPort.queryPointOptionById(pointOptionId) } returns pointOptionStub
        val name = "updated"
        val score = 20

        // when & then
        assertDoesNotThrow {
            updatePointOptionUseCase.execute(pointOptionId, name, score)
        }
    }

    @Test
    fun `상벌점 항목을 찾을 수 없음`() {
        // given
        every { securityPort.getCurrentUserId() } returns currentUserId
        every { queryUserPort.queryUserById(currentUserId) } returns userStub
        every { queryPointOptionPort.queryPointOptionById(pointOptionId) } returns null
        val name = "updated"
        val score = 20

        // when & then
        assertThrows<PointOptionNotFoundException> {
            updatePointOptionUseCase.execute(pointOptionId, name, score)
        }
    }

    @Test
    fun `다른 학교 상벌점일 경우`() {
        // given
        every { securityPort.getCurrentUserId() } returns currentUserId
        every { queryUserPort.queryUserById(currentUserId) } returns userStub
        every { queryPointOptionPort.queryPointOptionById(pointOptionId) } returns otherPointOptionStub
        val name = "updated"
        val score = 20

        // when & then
        assertThrows<SchoolMismatchException> {
            updatePointOptionUseCase.execute(pointOptionId, name, score)
        }
    }

    @Test
    fun `유저를 찾을 수 없음`() {
        // given
        every { securityPort.getCurrentUserId() } returns currentUserId
        every { queryUserPort.queryUserById(currentUserId) } returns null
        val name = "updated"
        val score = 20

        // when & then
        assertThrows<UserNotFoundException> {
            updatePointOptionUseCase.execute(pointOptionId, name, score)
        }
    }
}
