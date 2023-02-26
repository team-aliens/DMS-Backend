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
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.user.dto.UpdateUserPasswordRequest
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import team.aliens.dms.domain.user.spi.CommandUserPort
import team.aliens.dms.domain.user.spi.QueryUserPort
import team.aliens.dms.domain.user.spi.UserSecurityPort
import java.time.LocalDateTime
import java.util.UUID

@ExtendWith(SpringExtension::class)
class UpdateUserPasswordUseCaseTests {

    @MockBean
    private lateinit var securityPort: UserSecurityPort

    @MockBean
    private lateinit var queryUserPort: QueryUserPort

    @MockBean
    private lateinit var commandUserPort: CommandUserPort

    private lateinit var updateUserPasswordUseCase: UpdateUserPasswordUseCase

    @BeforeEach
    fun setUp() {
        updateUserPasswordUseCase = UpdateUserPasswordUseCase(
            securityPort, queryUserPort, commandUserPort
        )
    }

    private val currentUserId = UUID.randomUUID()
    private val oldPassword = "예전 비밀번호"
    private val newPassword = "새로운 비밀번호"

    private val updateUserPasswordRequest by lazy {
        UpdateUserPasswordRequest(
            currentPassword = oldPassword,
            newPassword = newPassword
        )
    }

    private val userStub by lazy {
        User(
            schoolId = UUID.randomUUID(),
            accountId = "계정아이디",
            password = oldPassword,
            email = "이메일",
            authority = Authority.STUDENT,
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )
    }

    @Test
    fun `비밀번호 변경 성공`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(userStub)

        given(securityPort.isPasswordMatch(updateUserPasswordRequest.currentPassword, oldPassword))
            .willReturn(true)

        given(securityPort.encodePassword(updateUserPasswordRequest.newPassword))
            .willReturn(newPassword)

        given(commandUserPort.saveUser(userStub.copy(password = newPassword)))
            .willReturn(userStub)

        // when & then
        assertDoesNotThrow {
            updateUserPasswordUseCase.execute(updateUserPasswordRequest)
        }
    }

    @Test
    fun `유저 미존재`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(null)

        // when & then
        assertThrows<UserNotFoundException> {
            updateUserPasswordUseCase.execute(updateUserPasswordRequest)
        }
    }

    @Test
    fun `예전 비밀번호 불일치`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(currentUserId)

        given(queryUserPort.queryUserById(currentUserId))
            .willReturn(userStub)

        given(securityPort.isPasswordMatch("이상한 비밀번호", userStub.password))
            .willReturn(false)

        // when & then
        assertThrows<PasswordMismatchException> {
            updateUserPasswordUseCase.execute(updateUserPasswordRequest)
        }
    }
}
