package team.aliens.dms.domain.auth.usecase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.auth.spi.AuthQueryUserPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import java.time.LocalDateTime
import java.util.UUID

@ExtendWith(SpringExtension::class)
class CheckAccountIdExistenceUseCaseTests {

    @MockBean
    private lateinit var authQueryUserPort: AuthQueryUserPort

    private lateinit var checkAccountIdExistenceUseCase: CheckAccountIdExistenceUseCase

    @BeforeEach
    fun setUp() {
        checkAccountIdExistenceUseCase = CheckAccountIdExistenceUseCase(
            authQueryUserPort
        )
    }

    private val accountId = "계정 아이디임"

    private val userStub by lazy {
        User(
            id = UUID.randomUUID(),
            schoolId = UUID.randomUUID(),
            accountId = accountId,
            password = "비밀번호임",
            email = "이메일임",
            authority = Authority.STUDENT,
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )
    }

    @Test
    fun `유저 존재함`() {
        // given
        given(authQueryUserPort.queryUserByAccountId(accountId))
            .willReturn(userStub)

        // when & then
        assertDoesNotThrow {
            checkAccountIdExistenceUseCase.execute(accountId)
        }
    }

    @Test
    fun `유저 존재하지 않음`() {
        // given
        given(authQueryUserPort.queryUserByAccountId(accountId))
            .willReturn(null)

        // when & then
        assertThrows<UserNotFoundException> {
            checkAccountIdExistenceUseCase.execute(accountId)
        }
    }
}