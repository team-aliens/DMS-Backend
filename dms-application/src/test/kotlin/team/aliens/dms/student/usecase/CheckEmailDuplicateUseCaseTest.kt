package team.aliens.dms.student.usecase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.user.exception.UserEmailExistsException
import team.aliens.dms.domain.student.spi.StudentQueryUserPort
import team.aliens.dms.domain.student.usecase.CheckEmailDuplicateUseCase

@ExtendWith(SpringExtension::class)
class CheckEmailDuplicateUseCaseTest {

    @MockBean
    private lateinit var studentQueryUserPort: StudentQueryUserPort

    private lateinit var checkEmailDuplicateUseCase: CheckEmailDuplicateUseCase

    private val email = "test123@dsm.hs.kr"

    @BeforeEach
    fun setUp() {
        checkEmailDuplicateUseCase = CheckEmailDuplicateUseCase(studentQueryUserPort)
    }

    @Test
    fun `이메일 중복 없음`() {
        given(studentQueryUserPort.existsByEmail(email))
            .willReturn(false)

        assertDoesNotThrow {
            checkEmailDuplicateUseCase.execute(email)
        }
    }

    @Test
    fun `이메일 중복`() {
        given(studentQueryUserPort.existsByEmail(email))
            .willReturn(true)

        assertThrows<UserEmailExistsException> {
            checkEmailDuplicateUseCase.execute(email)
        }
    }
}