package team.aliens.dms.domain.auth.usecase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.dto.CertifyEmailRequest
import team.aliens.dms.domain.auth.exception.EmailMismatchException
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.auth.spi.AuthQueryUserPort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import java.util.UUID

@ExtendWith(SpringExtension::class)
class CertifyEmailUseCaseTests {

    @MockBean
    private lateinit var queryUserPort: AuthQueryUserPort

    private lateinit var certifyEmailUseCase: CertifyEmailUseCase

    @BeforeEach
    fun setUp() {
        certifyEmailUseCase = CertifyEmailUseCase(queryUserPort)
    }

    private val id = UUID.randomUUID()
    private val email = "email@dsm.hs.kr"
    private val accountId = "accountId"

    private val userStub by lazy {
        User(
            id = id,
            schoolId = id,
            accountId = accountId,
            password = "password",
            email = email,
            authority = Authority.STUDENT,
            createdAt = null,
            deletedAt = null
        )
    }

    @Test
    fun `이메일 검증 성공`() {
        val request = CertifyEmailRequest(accountId, email)

        //given
        given(queryUserPort.queryUserByAccountId(request.accountId))
            .willReturn(userStub)

        //when then
        assertDoesNotThrow {
            certifyEmailUseCase.execute(request)
        }
    }

    @Test
    fun `유저를 찾을 수 없음`() {
        val request = CertifyEmailRequest(accountId, email)

        //given
        given(queryUserPort.queryUserByAccountId(request.accountId))
            .willReturn(null)

        //when then
        assertThrows<UserNotFoundException> {
            certifyEmailUseCase.execute(request)
        }
    }

    @Test
    fun `이메일이 일치하지 않음`() {
        val mismatchEmail = "mis@match.com"
        val request = CertifyEmailRequest(accountId, mismatchEmail)

        //given
        given(queryUserPort.queryUserByAccountId(request.accountId))
            .willReturn(userStub)

        //when then
        assertThrows<EmailMismatchException> {
            certifyEmailUseCase.execute(request)
        }
    }
}