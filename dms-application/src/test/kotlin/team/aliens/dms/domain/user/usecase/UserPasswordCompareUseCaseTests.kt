package team.aliens.dms.domain.user.usecase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.exception.PasswordMismatchException
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import team.aliens.dms.domain.user.spi.QueryUserPort
import team.aliens.dms.domain.user.spi.UserSecurityPort
import java.time.LocalDateTime
import java.util.UUID

@ExtendWith(SpringExtension::class)
class UserPasswordCompareUseCaseTests {

    @MockBean
    private lateinit var securityPort: UserSecurityPort

    @MockBean
    private lateinit var queryUserPort: QueryUserPort

    private lateinit var userPasswordCompareUseCase: UserPasswordCompareUseCase

    @BeforeEach
    fun setUp() {
        userPasswordCompareUseCase = UserPasswordCompareUseCase(
            securityPort, queryUserPort
        )
    }

    private val currentUserId = UUID.randomUUID()
    private val password = "비밀번호"

    private val userStub by lazy {
        User(
            id = UUID.randomUUID(),
            schoolId = UUID.randomUUID(),
            accountId = "아이디",
            password = password,
            email = "이메일",
            name = "이름",
            profileImageUrl = "https://~",
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )
    }

    @Test
    fun `비밀번호 일치`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(userStub)

        given(securityPort.isPasswordMatch(password, userStub.password))
            .willReturn(true)

        // when & then
        assertDoesNotThrow {
            userPasswordCompareUseCase.execute(password)
        }
    }

    @Test
    fun `사용자 미존재`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(null)

        // when & then
        assertThrows<UserNotFoundException> {
            userPasswordCompareUseCase.execute(password)
        }
    }

    @Test
    fun `비밀번호 불일치`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(userStub.copy(password = "이상한 비밀번호"))

        given(securityPort.isPasswordMatch(password, userStub.password))
            .willReturn(false)

        // when & then
        assertThrows<PasswordMismatchException> {
            userPasswordCompareUseCase.execute(password)
        }
    }
}