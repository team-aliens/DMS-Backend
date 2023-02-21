package team.aliens.dms.domain.point.usecase

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.manager.model.Manager
import team.aliens.dms.domain.point.exception.PointOptionNotFoundException
import team.aliens.dms.domain.point.exception.PointOptionSchoolMismatchException
import team.aliens.dms.domain.point.model.PointOption
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.CommandPointOptionPort
import team.aliens.dms.domain.point.spi.PointQueryManagerPort
import team.aliens.dms.domain.point.spi.PointSecurityPort
import team.aliens.dms.domain.point.spi.QueryPointOptionPort
import java.util.UUID

@ExtendWith(SpringExtension::class)
class RemovePointOptionUseCaseTest {

    private val securityPort: PointSecurityPort = mockk()
    private val queryManagerPort: PointQueryManagerPort = mockk()
    private val queryPointOptionPort: QueryPointOptionPort = mockk()
    private val commandPointOptionPort: CommandPointOptionPort = mockk(relaxed = true)

    private val removePointOptionUseCase = RemovePointOptionUseCase(
        securityPort, queryManagerPort, queryPointOptionPort, commandPointOptionPort
    )

    private val managerId = UUID.randomUUID()
    private val currentUserId = managerId
    private val schoolId = UUID.randomUUID()
    private val schoolId2 = UUID.randomUUID()
    private val pointOptionId = UUID.randomUUID()

    private val managerStub by lazy {
        Manager(
            id = managerId,
            schoolId = schoolId,
            name = "메니저"
        )
    }

    private val pointOptionStub by lazy {
        PointOption(
            name = "호실 청소 우수",
            type = PointType.BONUS,
            score = 10,
            schoolId = schoolId
        )
    }

    private val otherPointOptionStub by lazy {
        PointOption(
            name = "호실 청소 우수",
            type = PointType.BONUS,
            score = 10,
            schoolId = schoolId2
        )
    }

    @Test
    fun `상벌점 항목 삭제 성공`() {
        //given
        every { securityPort.getCurrentUserId() } returns currentUserId

        every { queryManagerPort.queryManagerById(managerId) } returns managerStub

        every { queryPointOptionPort.queryPointOptionById(pointOptionId) } returns pointOptionStub

        // when & then
        assertDoesNotThrow {
            removePointOptionUseCase.execute(pointOptionId)
        }
    }

    @Test
    fun `상벌점 항목 미존재`() {
        //given
        every { securityPort.getCurrentUserId() } returns currentUserId

        every { queryManagerPort.queryManagerById(managerId) } returns managerStub

        every { queryPointOptionPort.queryPointOptionById(pointOptionId) } returns null

        // when & then
        assertThrows<PointOptionNotFoundException> {
            removePointOptionUseCase.execute(pointOptionId)
        }
    }

    @Test
    fun `학교 불일치`() {
        //given
        every { securityPort.getCurrentUserId() } returns currentUserId

        every { queryManagerPort.queryManagerById(managerId) } returns managerStub

        every { queryPointOptionPort.queryPointOptionById(pointOptionId) } returns otherPointOptionStub

        // when & then
        assertThrows<PointOptionSchoolMismatchException> {
            removePointOptionUseCase.execute(pointOptionId)
        }
    }

    @Test
    fun `관리자 미존재`() {
        // given
        every { securityPort.getCurrentUserId() } returns currentUserId

        every { queryManagerPort.queryManagerById(managerId) } returns null


        assertThrows<ManagerNotFoundException> {
            removePointOptionUseCase.execute(pointOptionId)
        }
    }
}