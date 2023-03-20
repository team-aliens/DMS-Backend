package team.aliens.dms.domain.remain.usecase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.remain.dto.UpdateRemainAvailableTimeRequest
import team.aliens.dms.domain.remain.spi.CommandRemainAvailableTimePort
import team.aliens.dms.domain.remain.spi.RemainQueryUserPort
import team.aliens.dms.domain.remain.spi.RemainSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.stub.createUserStub
import java.time.DayOfWeek
import java.time.LocalTime
import java.util.UUID

@ExtendWith(SpringExtension::class)
class UpdateRemainAvailableTimeUseCaseTests {

    @MockBean
    private lateinit var securityPort: RemainSecurityPort

    @MockBean
    private lateinit var queryUserPort: RemainQueryUserPort

    @MockBean
    private lateinit var commandRemainAvailableTimePort: CommandRemainAvailableTimePort

    private lateinit var updateRemainAvailableTimeUseCase: UpdateRemainAvailableTimeUseCase

    @BeforeEach
    fun setUp() {
        updateRemainAvailableTimeUseCase = UpdateRemainAvailableTimeUseCase(
            securityPort,
            queryUserPort,
            commandRemainAvailableTimePort
        )
    }

    private val userId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()

    private val userStub by lazy {
        createUserStub(
            id = userId, schoolId = schoolId, authority = Authority.MANAGER
        )
    }

    private val requestStub by lazy {
        UpdateRemainAvailableTimeRequest(
            startDayOfWeek = DayOfWeek.MONDAY,
            startTime = LocalTime.MIN,
            endDayOfWeek = DayOfWeek.TUESDAY,
            endTime = LocalTime.now()
        )
    }

    @Test
    fun `잔류 신청 시간 생성 성공`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(userId)

        given(queryUserPort.queryUserById(userId))
            .willReturn(userStub)

        // when & then
        assertDoesNotThrow {
            updateRemainAvailableTimeUseCase.execute(requestStub)
        }
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
            updateRemainAvailableTimeUseCase.execute(requestStub)
        }
    }
}
