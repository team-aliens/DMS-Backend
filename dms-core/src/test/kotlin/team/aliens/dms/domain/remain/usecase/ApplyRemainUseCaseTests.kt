package team.aliens.dms.domain.remain.usecase

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.remain.exception.RemainCanNotAppliedException
import team.aliens.dms.domain.remain.exception.RemainOptionNotFoundException
import team.aliens.dms.domain.remain.model.RemainAvailableTime
import team.aliens.dms.domain.remain.model.RemainOption
import team.aliens.dms.domain.remain.spi.CommandRemainStatusPort
import team.aliens.dms.domain.remain.spi.QueryRemainAvailableTimePort
import team.aliens.dms.domain.remain.spi.QueryRemainOptionPort
import team.aliens.dms.domain.remain.spi.RemainQueryUserPort
import team.aliens.dms.domain.remain.spi.RemainSecurityPort
import team.aliens.dms.domain.remain.stub.createRemainOptionStub
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.stub.createUserStub
import java.util.UUID

@ExtendWith(SpringExtension::class)
class ApplyRemainUseCaseTests {

    private val securityPort: RemainSecurityPort = mockk(relaxed = true)
    private val queryUserPort: RemainQueryUserPort = mockk(relaxed = true)
    private val queryRemainOptionPort: QueryRemainOptionPort = mockk(relaxed = true)
    private val queryRemainAvailableTimePort: QueryRemainAvailableTimePort = mockk(relaxed = true)
    private val commandRemainStatusPort: CommandRemainStatusPort = mockk(relaxed = true)

    private val applyRemainUseCase = ApplyRemainUseCase(
        securityPort, queryUserPort, queryRemainOptionPort, queryRemainAvailableTimePort, commandRemainStatusPort
    )

    private val userId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()

    private val userStub by lazy {
        createUserStub(
            id = userId,
            schoolId = schoolId,
            authority = Authority.MANAGER
        )
    }

    private val remainOptionId = UUID.randomUUID()

    private val remainOptionStub by lazy {
        createRemainOptionStub(
            id = remainOptionId,
            schoolId = schoolId
        )
    }

    private val remainAvailableTimeStub: RemainAvailableTime = mockk()

    @Test
    fun `잔류 신청 성공`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryRemainOptionPort.queryRemainOptionById(remainOptionId) } returns remainOptionStub
        every {
            queryRemainAvailableTimePort.queryRemainAvailableTimeBySchoolId(schoolId)
        } returns remainAvailableTimeStub
        every { remainAvailableTimeStub.isAvailable() } returns true

        // when & then
        assertDoesNotThrow {
            applyRemainUseCase.execute(remainOptionId)
        }
    }

    @Test
    fun `유저가 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns null

        // when & then
        assertThrows<UserNotFoundException> {
            applyRemainUseCase.execute(remainOptionId)
        }
    }

    @Test
    fun `잔류 신청 가능 시간이 아님`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryRemainOptionPort.queryRemainOptionById(remainOptionId) } returns remainOptionStub
        every {
            queryRemainAvailableTimePort.queryRemainAvailableTimeBySchoolId(schoolId)
        } returns remainAvailableTimeStub
        every { remainAvailableTimeStub.isAvailable() } returns false

        // when & then
        assertThrows<RemainCanNotAppliedException> {
            applyRemainUseCase.execute(remainOptionId)
        }
    }

    @Test
    fun `잔류 항목이 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryRemainOptionPort.queryRemainOptionById(remainOptionId) } returns null

        // when & then
        assertThrows<RemainOptionNotFoundException> {
            applyRemainUseCase.execute(remainOptionId)
        }
    }

    private val otherRemainOptionStub by lazy {
        RemainOption(
            id = remainOptionId,
            schoolId = UUID.randomUUID(),
            title = "",
            description = ""
        )
    }

    @Test
    fun `다른 학교의 잔류 항목임`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryRemainOptionPort.queryRemainOptionById(remainOptionId) } returns otherRemainOptionStub
        every {
            queryRemainAvailableTimePort.queryRemainAvailableTimeBySchoolId(schoolId)
        } returns remainAvailableTimeStub
        every { remainAvailableTimeStub.isAvailable() } returns true

        // when & then
        assertThrows<SchoolMismatchException> {
            applyRemainUseCase.execute(remainOptionId)
        }
    }
}
