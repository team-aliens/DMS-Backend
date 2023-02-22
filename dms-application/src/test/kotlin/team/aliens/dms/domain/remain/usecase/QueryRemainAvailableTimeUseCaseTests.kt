package team.aliens.dms.domain.remain.usecase

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.model.Authority
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

    @MockBean
    private lateinit var securityPort: RemainSecurityPort

    @MockBean
    private lateinit var queryUserPort: RemainQueryUserPort

    @MockBean
    private lateinit var queryRemainAvailableTimePort: QueryRemainAvailableTimePort

    private lateinit var queryRemainAvailableTimeUseCase: QueryRemainAvailableTimeUseCase

    @BeforeEach
    fun setUp() {
        queryRemainAvailableTimeUseCase = QueryRemainAvailableTimeUseCase(
            securityPort,
            queryUserPort,
            queryRemainAvailableTimePort
        )
    }

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

    private val successRemainAvailableTimeStub by lazy {
        RemainAvailableTime(
            id = schoolId,
            startDayOfWeek = DayOfWeek.WEDNESDAY,
            startTime = LocalTime.of(0,0),
            endDayOfWeek = DayOfWeek.FRIDAY,
            endTime = LocalTime.of(23, 59)
        )
    }

    private val failureRemainAvailableTimeStub by lazy {
        RemainAvailableTime(
            id = schoolId,
            startDayOfWeek = DayOfWeek.MONDAY,
            startTime = LocalTime.of(0,0),
            endDayOfWeek = DayOfWeek.MONDAY,
            endTime = LocalTime.of(23, 59)
        )
    }

    @Test
    fun `잔류 신청 시간 보기 성공`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(userId)

        given(queryUserPort.queryUserById(userId))
            .willReturn(userStub)

        given(queryRemainAvailableTimePort.queryRemainAvailableTimeBySchoolId(schoolId))
            .willReturn(successRemainAvailableTimeStub)

        // when
        val isAccessible = successRemainAvailableTimeStub.isAvailable()

        // then
        assertThat(isAccessible).isTrue

        assertDoesNotThrow {
            queryRemainAvailableTimeUseCase.execute()
        }
    }

    @Test
    fun `잔류 신청 시간 보기 실패`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(userId)

        given(queryUserPort.queryUserById(userId))
            .willReturn(userStub)

        given(queryRemainAvailableTimePort.queryRemainAvailableTimeBySchoolId(schoolId))
            .willReturn(failureRemainAvailableTimeStub)

        // when
        val isAccessible = failureRemainAvailableTimeStub.isAvailable()

        // then
        assertThat(isAccessible).isFalse
    }

    @Test
    fun `유저를 찾지 못함`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(userId)

        given(queryUserPort.queryUserById(userId))
            .willReturn(null)

        // when & then
        assertThrows<UserNotFoundException> {
            queryRemainAvailableTimeUseCase.execute()
        }
    }
}
