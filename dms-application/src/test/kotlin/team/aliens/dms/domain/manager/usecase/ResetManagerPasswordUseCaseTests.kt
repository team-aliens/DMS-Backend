package team.aliens.dms.domain.manager.usecase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.exception.AuthCodeMismatchException
import team.aliens.dms.domain.auth.exception.AuthCodeNotFoundException
import team.aliens.dms.domain.auth.model.AuthCode
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.auth.model.EmailType
import team.aliens.dms.domain.manager.dto.ResetManagerPasswordRequest
import team.aliens.dms.domain.manager.exception.ManagerInfoMismatchException
import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.manager.spi.ManagerCommandUserPort
import team.aliens.dms.domain.manager.spi.ManagerQueryAuthCodePort
import team.aliens.dms.domain.manager.spi.ManagerQueryUserPort
import team.aliens.dms.domain.manager.spi.ManagerSecurityPort
import team.aliens.dms.domain.user.exception.InvalidRoleException
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import team.aliens.dms.domain.user.service.CheckUserAuthority
import java.time.LocalDateTime
import java.util.UUID

@ExtendWith(SpringExtension::class)
class ResetManagerPasswordUseCaseTests {

    @MockBean
    private lateinit var queryUserPort: ManagerQueryUserPort

    @MockBean
    private lateinit var queryAuthCodePort: ManagerQueryAuthCodePort

    @MockBean
    private lateinit var commandUserPort: ManagerCommandUserPort

    @MockBean
    private lateinit var securityPort: ManagerSecurityPort

    @MockBean
    private lateinit var checkUserAuthority: CheckUserAuthority

    private lateinit var resetManagerPasswordUseCase: ResetManagerPasswordUseCase

    @BeforeEach
    fun setUp() {
        resetManagerPasswordUseCase = ResetManagerPasswordUseCase(
            queryUserPort,
            queryAuthCodePort,
            commandUserPort,
            securityPort,
            checkUserAuthority
        )
    }

    private val accountId = "dlwjddbs13"
    private val email = "dlwjddbs13@naver.com"
    private val code = "AUTH12"
    private val password = "???????????????"
    private val userId = UUID.randomUUID()

    private val userStub by lazy {
        User(
            id = userId,
            schoolId = UUID.randomUUID(),
            accountId = "111111",
            password = password,
            email = email,
            authority = Authority.MANAGER,
            createdAt = LocalDateTime.now(),
            deletedAt = null
        )
    }

    private val authCodeStub by lazy {
        AuthCode(
            code = code,
            email = email,
            type = EmailType.PASSWORD,
            expirationTime = 123
        )
    }

    private val requestStub by lazy {
        ResetManagerPasswordRequest(
            accountId = accountId,
            email = email,
            authCode = code,
            newPassword = password
        )
    }

    private val emailErrorRequestStub by lazy {
        ResetManagerPasswordRequest(
            accountId = accountId,
            email = "??????????????????",
            authCode = code,
            newPassword = password
        )
    }

    @Test
    fun `???????????? ??????`() {
        // given
        given(queryUserPort.queryUserByAccountId(requestStub.accountId))
            .willReturn(userStub)

        given(checkUserAuthority.execute(userStub.id))
            .willReturn(Authority.MANAGER)

        given(queryAuthCodePort.queryAuthCodeByEmail(requestStub.email))
            .willReturn(authCodeStub)

        given(securityPort.encodePassword(requestStub.newPassword))
            .willReturn(password)

        // when & then
        assertDoesNotThrow {
            resetManagerPasswordUseCase.execute(requestStub)
        }
    }

    @Test
    fun `???????????? ?????? ???????????? ??????`() {
        // given
        given(queryUserPort.queryUserByAccountId(requestStub.accountId))
            .willReturn(null)

        // when & then
        assertThrows<UserNotFoundException> {
            resetManagerPasswordUseCase.execute(requestStub)
        }
    }

    @Test
    fun `?????? ?????? ?????????`() {
        // given
        given(queryUserPort.queryUserByAccountId(requestStub.accountId))
            .willReturn(userStub)

        given(checkUserAuthority.execute(userStub.id))
            .willReturn(Authority.STUDENT)

        // when & then
        assertThrows<InvalidRoleException> {
            resetManagerPasswordUseCase.execute(requestStub)
        }
    }

    @Test
    fun `????????? ?????????`() {
        // given
        given(queryUserPort.queryUserByAccountId(requestStub.accountId))
            .willReturn(userStub)

        given(checkUserAuthority.execute(userStub.id))
            .willReturn(Authority.MANAGER)

        // when & then
        assertThrows<ManagerInfoMismatchException> {
            resetManagerPasswordUseCase.execute(emailErrorRequestStub)
        }
    }

    @Test
    fun `???????????? ???????????? ??????`() {
        // given
        given(queryUserPort.queryUserByAccountId(requestStub.accountId))
            .willReturn(userStub)

        given(checkUserAuthority.execute(userStub.id))
            .willReturn(Authority.MANAGER)

        given(queryAuthCodePort.queryAuthCodeByEmail(requestStub.email))
            .willReturn(null)

        // when & then
        assertThrows<AuthCodeNotFoundException> {
            resetManagerPasswordUseCase.execute(requestStub)
        }
    }

    @Test
    fun `????????? ???????????? ?????????`() {
        // given
        given(queryUserPort.queryUserByAccountId(requestStub.accountId))
            .willReturn(userStub)

        given(checkUserAuthority.execute(userStub.id))
            .willReturn(Authority.MANAGER)

        given(queryAuthCodePort.queryAuthCodeByEmail(requestStub.email))
            .willReturn(authCodeStub.copy(code = "??????????????????"))

        // when & then
        assertThrows<AuthCodeMismatchException> {
            resetManagerPasswordUseCase.execute(requestStub)
        }
    }
}