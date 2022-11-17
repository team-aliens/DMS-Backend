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
import team.aliens.dms.domain.user.exception.UserEmailExistsException

@ExtendWith(SpringExtension::class)
class CheckDuplicatedEmailUseCaseTests {

    @MockBean
    private lateinit var studentQueryUserPort: StudentQueryUserPort

    private lateinit var checkDuplicatedEmailUseCase: CheckDuplicatedEmailUseCase

    @BeforeEach
    fun setUp() {
        checkDuplicatedEmailUseCase = CheckDuplicatedEmailUseCase(studentQueryUserPort)
    }

    private val email = "test123@dsm.hs.kr"

    @Test
    fun `이메일 중복 없음`() {
        // given
        given(studentQueryUserPort.existsUserByEmail(email))
            .willReturn(false)

        // when & then
        assertDoesNotThrow {
            checkDuplicatedEmailUseCase.execute(email)
        }
    }

    @Test
    fun `이메일 중복`() {
        // given
        given(studentQueryUserPort.existsUserByEmail(email))
            .willReturn(true)

        // when & then
        assertThrows<UserEmailExistsException> {
            checkDuplicatedEmailUseCase.execute(email)
        }
    }
}