package team.aliens.dms.domain.point.usecase

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.manager.stub.createManagerStub
import team.aliens.dms.domain.point.dto.CreatePointOptionRequest
import team.aliens.dms.domain.point.exception.PointOptionNameExistsException
import team.aliens.dms.domain.point.spi.CommandPointOptionPort
import team.aliens.dms.domain.point.spi.PointQueryManagerPort
import team.aliens.dms.domain.point.spi.PointSecurityPort
import team.aliens.dms.domain.point.spi.QueryPointOptionPort
import java.util.UUID

@ExtendWith(SpringExtension::class)
class CreatePointOptionUseCaseTest {

    private val securityPort: PointSecurityPort = mockk()
    private val queryManagerPort: PointQueryManagerPort = mockk()
    private val queryPointOptionPort: QueryPointOptionPort = mockk()
    private val commandPointOptionPort: CommandPointOptionPort = mockk(relaxed = true)

    private val createPointOptionUseCase = CreatePointOptionUseCase(
        securityPort, queryManagerPort, commandPointOptionPort, queryPointOptionPort
    )

    private val managerId = UUID.randomUUID()
    private val currentUserId = managerId
    private val schoolId = UUID.randomUUID()

    private val managerStub by lazy {
        createManagerStub(
            id = managerId,
            schoolId = schoolId
        )
    }

    private val requestStub by lazy {
        CreatePointOptionRequest(
            name = "호실 청소 우수",
            type = "BONUS",
            score = 10
        )
    }

    @Test
    fun `상벌점 항목 생성 성공`() {
        // given
        every { securityPort.getCurrentUserId() } returns currentUserId

        every { queryManagerPort.queryManagerById(managerId) } returns managerStub

        every { queryPointOptionPort.existByNameAndSchoolId(requestStub.name, schoolId) } returns false

        // when & then
        assertDoesNotThrow {
            createPointOptionUseCase.execute(requestStub)
        }
    }

    @Test
    fun `항목이 이미 존재하는 경우`() {
        // given
        every { securityPort.getCurrentUserId() } returns currentUserId

        every { queryManagerPort.queryManagerById(managerId) } returns managerStub

        every { queryPointOptionPort.existByNameAndSchoolId(requestStub.name, schoolId) } returns true

        // when & then
        assertThrows<PointOptionNameExistsException> {
            createPointOptionUseCase.execute(requestStub)
        }
    }

    @Test
    fun `관리자 미존재`() {
        // given
        every { securityPort.getCurrentUserId() } returns currentUserId

        every { queryManagerPort.queryManagerById(managerId) } returns null

        // when & then
        assertThrows<ManagerNotFoundException> {
            createPointOptionUseCase.execute(requestStub)
        }
    }
}
