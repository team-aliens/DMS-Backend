package team.aliens.dms.domain.tag.usecase

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.tag.spi.QueryTagPort
import team.aliens.dms.domain.tag.spi.TagQueryUserPort
import team.aliens.dms.domain.tag.spi.TagSecurityPort
import team.aliens.dms.domain.tag.stub.createTagStub
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.stub.createUserStub
import java.util.UUID

@ExtendWith(SpringExtension::class)
class QueryTagsUseCaseTests {

    private val queryTagPort: QueryTagPort = mockk()
    private val securityPort: TagSecurityPort = mockk()
    private val queryUserPort: TagQueryUserPort = mockk()

    private val queryTagsUseCase = QueryTagsUseCase(
        queryTagPort, securityPort, queryUserPort
    )

    private val currentUserId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()

    @Test
    fun `태그 조회 성공`() {
        // given
        every { securityPort.getCurrentUserId() } returns currentUserId
        every { queryUserPort.queryUserById(currentUserId) } returns createUserStub(
            id = currentUserId,
            schoolId = schoolId
        )
        every { queryTagPort.queryTagsBySchoolId(schoolId) } returns listOf(
            createTagStub(
                name = "테스트"
            )
        )

        // when & then
        assertDoesNotThrow {
            queryTagsUseCase.execute()
        }
    }

    @Test
    fun `사용자를 찾을 수 없음`() {
        // given
        every { securityPort.getCurrentUserId() } returns currentUserId
        every { queryUserPort.queryUserById(currentUserId) } returns null

        // when & then
        assertThrows<UserNotFoundException> {
            queryTagsUseCase.execute()
        }
    }
}
