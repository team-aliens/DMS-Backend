package team.aliens.dms.domain.student.usecase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.student.spi.StudentQueryUserPort
import team.aliens.dms.domain.user.exception.UserAccountIdExistsException

@ExtendWith(SpringExtension::class)
class CheckDuplicatedAccountIdUseCaseTests {

    @MockBean
    private lateinit var studentQueryUserPort: StudentQueryUserPort

    private lateinit var checkDuplicatedAccountIdUseCase: CheckDuplicatedAccountIdUseCase

    @BeforeEach
    fun setUp() {
        checkDuplicatedAccountIdUseCase = CheckDuplicatedAccountIdUseCase(studentQueryUserPort)
    }

    private val accountId = "계정아이디입니다하하하"

    @Test
    fun `계정 아이디 중복 없음`() {
        // given
        given(studentQueryUserPort.existsUserByAccountId(accountId))
            .willReturn(false)

        // when & then
        assertDoesNotThrow {
            checkDuplicatedAccountIdUseCase.execute(accountId)
        }
    }

    @Test
    fun `계정 아이디 중복`() {
        // given
        given(studentQueryUserPort.existsUserByAccountId(accountId))
            .willReturn(true)

        // when & then
        assertThrows<UserAccountIdExistsException> {
            checkDuplicatedAccountIdUseCase.execute(accountId)
        }
    }
}