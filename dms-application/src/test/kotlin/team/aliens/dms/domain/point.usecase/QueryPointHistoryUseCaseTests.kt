package team.aliens.dms.domain.point.usecase

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.point.dto.PointRequestType
import team.aliens.dms.domain.point.dto.QueryPointHistoryResponse
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.PointQueryUserPort
import team.aliens.dms.domain.point.spi.PointSecurityPort
import team.aliens.dms.domain.point.spi.QueryPointPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

@ExtendWith(SpringExtension::class)
class QueryPointHistoryUseCaseTests {

    @MockBean
    private lateinit var queryPointPort: QueryPointPort

    @MockBean
    private lateinit var queryUserPort: PointQueryUserPort

    @MockBean
    private lateinit var securityPort: PointSecurityPort

    private lateinit var queryPointHistoryUseCase: QueryPointHistoryUseCase

    @BeforeEach
    fun setUp() {
        queryPointHistoryUseCase = QueryPointHistoryUseCase(
            queryPointPort, queryUserPort, securityPort
        )
    }

    private val currentUserId = UUID.randomUUID()

    private val userStub by lazy {
        User(
            id = currentUserId,
            schoolId = UUID.randomUUID(),
            accountId = "test accountId",
            password = "test password",
            email = "test email",
            name = "test name",
            profileImageUrl = "https://~",
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )
    }

    private val pointHistoryVOs by lazy {
        listOf(
            QueryPointHistoryResponse.Point(
                pointId = UUID.randomUUID(),
                date = LocalDate.now(),
                type = PointType.BONUS,
                name = "test name",
                score = 10
            ),
            QueryPointHistoryResponse.Point(
                pointId = UUID.randomUUID(),
                date = LocalDate.now(),
                type = PointType.MINUS,
                name = "test name2",
                score = -5
            )
        )
    }

    @Test
    fun `상벌점 내역 조회 성공(BONUS)`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(userStub)

        given(queryPointPort.queryPointHistoryByStudentIdAndType(currentUserId, PointType.BONUS))
            .willReturn(pointHistoryVOs)

        // when
        val response = queryPointHistoryUseCase.execute(PointRequestType.BONUS)

        // then
        assertAll(
            { assert(response.totalPoint == 5) },
            { assertThat(response.points).isNotEmpty }
        )
    }

    @Test
    fun `상벌점 내역 조회 성공(MINUS)`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(userStub)

        given(queryPointPort.queryPointHistoryByStudentIdAndType(currentUserId, PointType.MINUS))
            .willReturn(pointHistoryVOs)

        // when
        val response = queryPointHistoryUseCase.execute(PointRequestType.MINUS)

        // then
        assertAll(
            { assert(response.totalPoint == 5) },
            { assertThat(response.points).isNotEmpty }
        )
    }

    @Test
    fun `상벌점 내역 조회 성공(ALL)`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(userStub)

        given(queryPointPort.queryAllPointHistoryByStudentId(currentUserId))
            .willReturn(pointHistoryVOs)

        // when
        val response = queryPointHistoryUseCase.execute(PointRequestType.ALL)

        // then
        assertAll(
            { assert(response.totalPoint == 5) },
            { assertThat(response.points).isNotEmpty }
        )
    }

    @Test
    fun `유저를 찾을 수 없음`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(null)

        // when & then
        assertThrows<UserNotFoundException> {
            queryPointHistoryUseCase.execute(PointRequestType.ALL)
        }
    }
}