package team.aliens.dms.domain.school.usecase

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.school.exception.FeatureNotFoundException
import team.aliens.dms.domain.school.model.AvailableFeature
import team.aliens.dms.domain.school.spi.QuerySchoolPort
import team.aliens.dms.domain.school.spi.SchoolQueryUserPort
import team.aliens.dms.domain.school.spi.SchoolSecurityPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import java.util.UUID

@ExtendWith(SpringExtension::class)
class QueryAvailableFeaturesUseCaseTest {

    private val securityPort: SchoolSecurityPort = mockk()
    private val queryUserPort: SchoolQueryUserPort = mockk()
    private val querySchoolPort: QuerySchoolPort = mockk()

    private val currentUserId = UUID.randomUUID()
    private val managerId = currentUserId
    private val schoolId = UUID.randomUUID()

    private val queryAvailableFeaturesUseCase = QueryAvailableFeaturesUseCase(
        securityPort, queryUserPort, querySchoolPort
    )

    private val userStub by lazy {
        User(
            id = managerId,
            schoolId = schoolId,
            accountId = "accountId",
            password = "password",
            email = "email",
            authority = Authority.MANAGER,
            createdAt = null,
            deletedAt = null
        )
    }

    private val availableFeatureStub by lazy {
        AvailableFeature(
            mealService = true,
            studyRoomService = true,
            remainService = true,
            pointService = true,
            noticeService = true,
            schoolId = schoolId
        )
    }

    @Test
    fun `사용가능 기능목록 조회 성공`() {
        // given
        every { securityPort.getCurrentUserId() } returns currentUserId
        every { queryUserPort.queryUserById(currentUserId) } returns userStub
        every { querySchoolPort.queryAvailableFeaturesBySchoolId(schoolId) } returns availableFeatureStub

        // when & then
        assertDoesNotThrow {
            queryAvailableFeaturesUseCase.execute()
        }
    }

    @Test
    fun `사용가능 기능을 찾을 수 없음`() {
        // given
        every { securityPort.getCurrentUserId() } returns currentUserId
        every { queryUserPort.queryUserById(currentUserId) } returns userStub
        every { querySchoolPort.queryAvailableFeaturesBySchoolId(schoolId) } returns null

        // when & then
        assertThrows<FeatureNotFoundException> {
            queryAvailableFeaturesUseCase.execute()
        }
    }

    @Test
    fun `유저를 찾을 수 없음`() {
        // given
        every { securityPort.getCurrentUserId() } returns managerId

        every { queryUserPort.queryUserById(managerId) } returns null

        // when & then
        assertThrows<UserNotFoundException> {
            queryAvailableFeaturesUseCase.execute()
        }
    }
}
