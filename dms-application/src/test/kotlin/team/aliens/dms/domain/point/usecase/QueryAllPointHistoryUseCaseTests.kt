package team.aliens.dms.domain.point.usecase

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.point.dto.PointRequestType
import team.aliens.dms.domain.point.dto.QueryAllPointHistoryResponse
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.PointQueryUserPort
import team.aliens.dms.domain.point.spi.PointSecurityPort
import team.aliens.dms.domain.point.spi.QueryPointHistoryPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.stub.createUserStub
import java.time.LocalDate
import java.util.UUID

@ExtendWith(SpringExtension::class)
class QueryAllPointHistoryUseCaseTests {

    private val securityPort: PointSecurityPort = mockk(relaxed = true)
    private val queryUserPort: PointQueryUserPort = mockk(relaxed = true)
    private val queryPointHistoryPort: QueryPointHistoryPort = mockk(relaxed = true)

    private val queryAllPointHistoryUseCase = QueryAllPointHistoryUseCase(
        securityPort, queryUserPort, queryPointHistoryPort
    )

    private val managerId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()

    private val userStub by lazy {
        createUserStub(
            id = managerId, schoolId = schoolId, authority =  Authority.MANAGER
        )
    }

    private val responseStub by lazy {
        QueryAllPointHistoryResponse.PointHistory(
            pointHistoryId = UUID.randomUUID(),
            studentName = "김은빈",
            studentGcn = "2106",
            date = LocalDate.now(),
            pointName = "타호실 출입",
            pointType = PointType.MINUS,
            pointScore = 3
        )
    }

    private val pointRequestType = PointRequestType.ALL

    private val pageData by lazy {
        PageData(
            page = 0,
            size = 10
        )
    }

    @Test
    fun `상벌점 내역 조회 성공`() {
        // given
        every { securityPort.getCurrentUserId() } returns managerId

        every { queryUserPort.queryUserById(managerId) } returns userStub

        every {
            queryPointHistoryPort.queryPointHistoryBySchoolIdAndType(
                schoolId = schoolId,
                type = null,
                isCancel = false,
                pageData = pageData
            )
        } returns listOf(responseStub)

        // when
        val response = queryAllPointHistoryUseCase.execute(pointRequestType, pageData)

        // then
        assertAll(
            { assertEquals(response.pointHistories.size, 1) },
            { assertEquals(response.pointHistories[0], responseStub) }
        )
    }

    @Test
    fun `유저가 존재하지 않음`() {
        // given
        every { securityPort.getCurrentUserId() } returns managerId

        every { queryUserPort.queryUserById(managerId) } returns null

        // when & then
        assertThrows<UserNotFoundException> {
            queryAllPointHistoryUseCase.execute(pointRequestType, pageData)
        }
    }
}
