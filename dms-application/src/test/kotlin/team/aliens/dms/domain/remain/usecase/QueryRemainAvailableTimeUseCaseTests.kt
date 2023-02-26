package team.aliens.dms.domain.remain.usecase

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.remain.exception.RemainAvailableTimeNotFoundException
import team.aliens.dms.domain.remain.model.RemainAvailableTime
import team.aliens.dms.domain.remain.spi.QueryRemainAvailableTimePort
import team.aliens.dms.domain.remain.spi.RemainQueryUserPort
import team.aliens.dms.domain.remain.spi.RemainSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.UUID

@ExtendWith(SpringExtension::class)
class QueryRemainAvailableTimeUseCaseTests {

    private val securityPort: RemainSecurityPort = mockk()
    private val queryUserPort: RemainQueryUserPort = mockk()
    private val queryRemainAvailableTimePort: QueryRemainAvailableTimePort = mockk()

    private val queryRemainAvailableTimeUseCase = QueryRemainAvailableTimeUseCase(
        securityPort, queryUserPort, queryRemainAvailableTimePort
    )

    private val userId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()

    private val userStub by lazy {
        User(
            id = userId,
            schoolId = schoolId,
            accountId = "accountId",
            password = "password",
            email = "email",
            authority = Authority.STUDENT,
            createdAt = null,
            deletedAt = null
        )
    }

    private val remainAvailableTimeStub by lazy {
        RemainAvailableTime(
            id = schoolId,
            startDayOfWeek = DayOfWeek.WEDNESDAY,
            startTime = LocalTime.of(0, 0),
            endDayOfWeek = DayOfWeek.FRIDAY,
            endTime = LocalTime.of(23, 59)
        )
    }

    @Test
    fun `잔류 신청 시간 보기 성공`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every {
            queryRemainAvailableTimePort.queryRemainAvailableTimeBySchoolId(schoolId)
        } returns remainAvailableTimeStub

        // when & then
        assertDoesNotThrow {
            queryRemainAvailableTimeUseCase.execute()
        }
    }

    @Test
    fun `잔류 신청 시간이 설정되지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns userStub
        every { queryRemainAvailableTimePort.queryRemainAvailableTimeBySchoolId(schoolId) } returns null

        // when
        assertThrows<RemainAvailableTimeNotFoundException> {
            queryRemainAvailableTimeUseCase.execute()
        }
    }

    @Test
    fun `유저가 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns userId
        every { queryUserPort.queryUserById(userId) } returns null

        // when & then
        assertThrows<UserNotFoundException> {
            queryRemainAvailableTimeUseCase.execute()
        }
    }
}
