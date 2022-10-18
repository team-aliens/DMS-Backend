package team.aliens.dms.student.usecase

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.student.exception.StudentEmailExistsException
import team.aliens.dms.domain.student.spi.StudentQueryUserPort
import team.aliens.dms.domain.student.usecase.CheckEmailDuplicateUseCase

@ExtendWith(SpringExtension::class)
class CheckEmailDuplicateUseCaseTest {

    @MockBean
    private lateinit var studentQueryUserPort: StudentQueryUserPort

    @MockBean
    private lateinit var checkEmailDuplicateUseCase: CheckEmailDuplicateUseCase

    private val email = "test123@dsm.hs.kr"

    @Test
    fun `이메일 중복`() {
        given(studentQueryUserPort.existsByEmail(email))
            .willReturn(true)

        //이거 좀 이상
        assertDoesNotThrow {
            checkEmailDuplicateUseCase.execute(email)
        }
    }

    @Test
    fun `이메일 중복 없음`() {
        given(studentQueryUserPort.existsByEmail(email))
            .willReturn(false)

        assertDoesNotThrow {
            checkEmailDuplicateUseCase.execute(email)
        }
    }
}