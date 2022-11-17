package team.aliens.dms.domain.auth.usecase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.dto.CertifyEmailCodeRequest
import team.aliens.dms.domain.auth.exception.AuthCodeMismatchException
import team.aliens.dms.domain.auth.exception.AuthCodeNotFoundException
import team.aliens.dms.domain.auth.model.AuthCode
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.auth.model.EmailType
import team.aliens.dms.domain.auth.spi.AuthQueryUserPort
import team.aliens.dms.domain.auth.spi.CommandAuthCodeLimitPort
import team.aliens.dms.domain.auth.spi.QueryAuthCodePort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import java.util.UUID

@ExtendWith(SpringExtension::class)
class CertifyEmailCodeUseCaseTests {

    @MockBean
    private lateinit var queryAuthCodePort: QueryAuthCodePort

    @MockBean
    private lateinit var queryUserPot: AuthQueryUserPort

    @MockBean
    private lateinit var commandAuthCodeLimitPort: CommandAuthCodeLimitPort

    private lateinit var certifyEmailCodeUseCase: CertifyEmailCodeUseCase

    @BeforeEach
    fun setUp() {
        certifyEmailCodeUseCase = CertifyEmailCodeUseCase(
            queryAuthCodePort, queryUserPot, commandAuthCodeLimitPort
        )
    }

    private val id = UUID.randomUUID()
    private val code = "123546"
    private val type = EmailType.PASSWORD
    private val email = "email@dsm.hs.kr"

    private val userStub by lazy {
        User(
            id = id,
            schoolId = id,
            accountId = "accountId",
            password = "password",
            email = email,
            authority = Authority.STUDENT,
            createdAt = null,
            deletedAt = null
        )
    }

    private val authCodeStub by lazy {
        AuthCode(
            code = code,
            email = email,
            type = type,
            expirationTime = 0
        )
    }

    @Test
    fun `이메일코드 확인 성공`() {
        val request = CertifyEmailCodeRequest(email, code, type)

        // given
        given(queryUserPot.queryUserByEmail(email))
            .willReturn(userStub)

        given(queryAuthCodePort.queryAuthCodeByEmailAndEmailType(email, type))
            .willReturn(authCodeStub)

        // when & then
        assertDoesNotThrow {
            certifyEmailCodeUseCase.execute(request)
        }
    }

    @Test
    fun `유저를 찾을 수 없지만 예외 발생 X`() {
        val request = CertifyEmailCodeRequest(email, code, EmailType.SIGNUP)

        val authCode = AuthCode(
            code = code,
            email = email,
            type = EmailType.SIGNUP,
            expirationTime = 0
        )

        // given
        given(queryUserPot.queryUserByEmail(email))
            .willReturn(null)

        given(queryAuthCodePort.queryAuthCodeByEmailAndEmailType(email, EmailType.SIGNUP))
            .willReturn(authCode)

        // when & then
        assertDoesNotThrow {
            certifyEmailCodeUseCase.execute(request)
        }
    }

    @Test
    fun `유저를 찾을 수 없음`() {
        val request = CertifyEmailCodeRequest(email, code, type)

        // given
        given(queryUserPot.queryUserByEmail(email))
            .willReturn(null)

        // when & then
        assertThrows<UserNotFoundException> {
            certifyEmailCodeUseCase.execute(request)
        }
    }

    @Test
    fun `인증코드를 찾을 수 없음`() {
        val request = CertifyEmailCodeRequest(email, code, type)

        // given
        given(queryUserPot.queryUserByEmail(email))
            .willReturn(userStub)

        given(queryAuthCodePort.queryAuthCodeByEmailAndEmailType(email, type))
            .willReturn(null)

        // when & then
        assertThrows<AuthCodeNotFoundException> {
            certifyEmailCodeUseCase.execute(request)
        }
    }

    @Test
    fun `인증코드 일치하지 않음`() {
        val notMatchedCode = "!@QWER"
        val request = CertifyEmailCodeRequest(email, notMatchedCode, type)

        // given
        given(queryUserPot.queryUserByEmail(email))
            .willReturn(userStub)

        given(queryAuthCodePort.queryAuthCodeByEmailAndEmailType(email, type))
            .willReturn(authCodeStub)

        //when & then
        assertThrows<AuthCodeMismatchException> {
            certifyEmailCodeUseCase.execute(request)
        }
    }
}