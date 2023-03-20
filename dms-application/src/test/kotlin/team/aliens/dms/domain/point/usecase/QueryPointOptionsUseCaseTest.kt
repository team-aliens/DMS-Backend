package team.aliens.dms.domain.point.usecase

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.manager.stub.createManagerStub
import team.aliens.dms.domain.point.dto.QueryPointOptionsResponse
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.PointQueryManagerPort
import team.aliens.dms.domain.point.spi.PointSecurityPort
import team.aliens.dms.domain.point.spi.QueryPointOptionPort
import team.aliens.dms.domain.point.stub.createPointOptionStub
import java.util.UUID

@ExtendWith(SpringExtension::class)
class QueryPointOptionsUseCaseTest {

    private val securityPort: PointSecurityPort = mockk()
    private val queryManagerPort: PointQueryManagerPort = mockk()
    private val queryPointOptionPort: QueryPointOptionPort = mockk()

    private val queryPointOptionsUseCase = QueryPointOptionsUseCase(
        securityPort, queryManagerPort, queryPointOptionPort
    )

    private val managerId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()
    private val pointOptionId = UUID.randomUUID()
    private val keyword = "호"

    private val managerStub by lazy {
        createManagerStub(id = managerId, schoolId = schoolId)
    }

    private val pointOptionStub by lazy {
        createPointOptionStub(id = pointOptionId, schoolId = schoolId)
    }

    private val responseStub by lazy {
        QueryPointOptionsResponse.PointOptionResponse(
            pointOptionId = pointOptionId,
            name = pointOptionStub.name,
            type = PointType.BONUS,
            score = pointOptionStub.score
        )
    }

    @Test
    fun `상벌점 항목 조회 성공`() {
        // given
        every { securityPort.getCurrentUserId() } returns managerId
        every { queryManagerPort.queryManagerById(managerId) } returns managerStub
        every {
            queryPointOptionPort.queryPointOptionsBySchoolIdAndKeyword(schoolId, keyword)
        } returns listOf(pointOptionStub)

        // when
        val response = queryPointOptionsUseCase.execute(keyword).pointOptions

        // then
        assertEquals(response[0], responseStub)
    }

    @Test
    fun `관리자 미존재`() {
        // given
        every { securityPort.getCurrentUserId() } returns managerId

        every { queryManagerPort.queryManagerById(managerId) } returns null

        // when & then
        assertThrows<ManagerNotFoundException> {
            queryPointOptionsUseCase.execute(keyword)
        }
    }
}
