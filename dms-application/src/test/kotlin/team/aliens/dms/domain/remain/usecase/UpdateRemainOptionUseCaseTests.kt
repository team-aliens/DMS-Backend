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
import team.aliens.dms.domain.remain.exception.RemainOptionNotFoundException
import team.aliens.dms.domain.remain.model.RemainOption
import team.aliens.dms.domain.remain.spi.CommandRemainOptionPort
import team.aliens.dms.domain.remain.spi.QueryRemainOptionPort
import team.aliens.dms.domain.remain.spi.RemainQueryUserPort
import team.aliens.dms.domain.remain.spi.RemainSecurityPort
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.user.model.User
import java.util.UUID

@ExtendWith(SpringExtension::class)
class UpdateRemainOptionUseCaseTests {

    @MockBean
    private lateinit var securityPort: RemainSecurityPort

    @MockBean
    private lateinit var queryUserPort: RemainQueryUserPort

    @MockBean
    private lateinit var queryRemainOptionPort: QueryRemainOptionPort

    @MockBean
    private lateinit var commandRemainOptionPort: CommandRemainOptionPort

    private lateinit var updateRemainOptionUseCase: UpdateRemainOptionUseCase

    @BeforeEach
    fun setUp() {
        updateRemainOptionUseCase = UpdateRemainOptionUseCase(
            securityPort, queryUserPort, queryRemainOptionPort, commandRemainOptionPort
        )
    }

    private val managerId = UUID.randomUUID()
    private val schoolId = UUID.randomUUID()

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

    private val remainOptionId = UUID.randomUUID()
    private val title = "title"
    private val description = "descripton"

    private val remainOptionStub by lazy {
        RemainOption(
            id = remainOptionId,
            schoolId = schoolId,
            title = title,
            description = description
        )
    }

    @Test
    fun `잔류항목 수정 성공`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(managerId)

        given(queryUserPort.queryUserById(managerId))
            .willReturn(userStub)

        given(queryRemainOptionPort.queryRemainOptionById(remainOptionId))
            .willReturn(remainOptionStub)

        //when & then
        assertDoesNotThrow {
            updateRemainOptionUseCase.execute(remainOptionId, title, description)
        }
    }

    @Test
    fun `잔류항목 존재하지 않음`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(managerId)

        given(queryUserPort.queryUserById(managerId))
            .willReturn(userStub)

        given(queryRemainOptionPort.queryRemainOptionById(remainOptionId))
            .willReturn(null)

        // when & then
        assertThrows<RemainOptionNotFoundException> {
            updateRemainOptionUseCase.execute(remainOptionId, title, description)
        }
    }

    private val otherRemainOptionStub by lazy {
        RemainOption(
            id = remainOptionId,
            schoolId = UUID.randomUUID(),
            title = title,
            description = description
        )
    }

    @Test
    fun `같은 학교의 매니저가 아님`() {
        given(securityPort.getCurrentUserId())
            .willReturn(managerId)

        given(queryUserPort.queryUserById(managerId))
            .willReturn(userStub)

        given(queryRemainOptionPort.queryRemainOptionById(remainOptionId))
            .willReturn(otherRemainOptionStub)

        // when & then
        assertThrows<SchoolMismatchException> {
            updateRemainOptionUseCase.execute(remainOptionId, title, description)
        }
    }
}