package team.aliens.dms.domain.auth.usecase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.auth.dto.SendEmailCodeRequest
import team.aliens.dms.domain.auth.exception.EmailAlreadyCertifiedException
import team.aliens.dms.domain.auth.model.AuthCodeLimit
import team.aliens.dms.domain.auth.model.EmailType
import team.aliens.dms.domain.auth.spi.*
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.model.User
import team.aliens.dms.common.spi.ReceiveRandomStringPort
import java.util.*

@ExtendWith(SpringExtension::class)
class SendEmailCodeUseCaseTests {

    @MockBean
    private lateinit var sendEmailPort: SendEmailPort

    @MockBean
    private lateinit var queryUserPort: AuthQueryUserPort

    @MockBean
    private lateinit var commandAuthCodePort: CommandAuthCodePort

    @MockBean
    private lateinit var receiveRandomStringPort: ReceiveRandomStringPort

    @MockBean
    private lateinit var queryAuthCodeLimitPort: QueryAuthCodeLimitPort

    @MockBean
    private lateinit var commandAuthCodeLimitPort: CommandAuthCodeLimitPort

    private lateinit var sendEmailCodeUseCase: SendEmailCodeUseCase

    @BeforeEach
    fun setUp() {
        sendEmailCodeUseCase = SendEmailCodeUseCase(
            sendEmailPort,
            queryUserPort,
            commandAuthCodePort,
            receiveRandomStringPort,
            queryAuthCodeLimitPort,
            commandAuthCodeLimitPort
        )
    }

    private val id = UUID.randomUUID()
    private val code = "@SF^%L"
    private val type = EmailType.PASSWORD
    private val email = "email@dsm.hs.kr"
    private val request = SendEmailCodeRequest(email, type)
    private val request2 = SendEmailCodeRequest(email, EmailType.SIGNUP)

    private val userStub by lazy {
        User(
            id = id,
            schoolId = id,
            accountId = "accountId",
            password = "password",
            email = email,
            name = "김범지인",
            profileImageUrl = "https://~~",
            createdAt = null,
            deletedAt = null
        )
    }

    @Test
    fun `인증번호 발송 성공 case1`() {
        val authCodeLimit = AuthCodeLimit(
            id = id,
            email = email,
            type = type,
            attemptCount = 0,
            isVerified = false,
            expirationTime = 1800
        )

        // given
        given(queryUserPort.queryUserByEmail(email))
            .willReturn(userStub)

        given(queryAuthCodeLimitPort.queryAuthCodeLimitByEmailAndEmailType(email, type))
            .willReturn(authCodeLimit)

        given(receiveRandomStringPort.randomNumber(6))
            .willReturn(code)

        // when & then
        assertDoesNotThrow {
            sendEmailCodeUseCase.execute(request)
        }
    }

    @Test
    fun `인증번호 발송 성공 case2`() {
        // given
        given(queryUserPort.queryUserByEmail(email))
            .willReturn(userStub)

        given(receiveRandomStringPort.randomNumber(6))
            .willReturn(code)

        // when & then
        assertDoesNotThrow {
            sendEmailCodeUseCase.execute(request)
        }
    }

    @Test
    fun `유저를 찾을 수 없지만 예외 발생 X`() {
        // given
        given(queryUserPort.queryUserByEmail(email))
            .willReturn(null)

        given(receiveRandomStringPort.randomNumber(6))
            .willReturn(code)

        // when & then
        assertDoesNotThrow {
            sendEmailCodeUseCase.execute(request2)
        }
    }

    @Test
    fun `유저를 찾을 수 없음`() {
        // given
        given(queryUserPort.queryUserByEmail(email))
            .willReturn(null)

        // when & then
        assertThrows<UserNotFoundException> {
            sendEmailCodeUseCase.execute(request)
        }
    }

    @Test
    fun `이미 인증됨`() {
        val authCodeLimit = AuthCodeLimit(
            id = id,
            email = email,
            type = type,
            attemptCount = 0,
            isVerified = true,
            expirationTime = 1800
        )

        // given
        given(queryUserPort.queryUserByEmail(email))
            .willReturn(userStub)

        given(queryAuthCodeLimitPort.queryAuthCodeLimitByEmailAndEmailType(email, type))
            .willReturn(authCodeLimit)

        assertThrows<EmailAlreadyCertifiedException> {
            sendEmailCodeUseCase.execute(request)
        }
    }
}