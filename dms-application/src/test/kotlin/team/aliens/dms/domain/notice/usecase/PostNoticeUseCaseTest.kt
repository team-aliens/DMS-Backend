package team.aliens.dms.domain.notice.usecase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.notice.spi.CommandNoticePort
import team.aliens.dms.domain.notice.spi.NoticeSecurityPort
import java.util.UUID

@ExtendWith(SpringExtension::class)
class PostNoticeUseCaseTest {

    @MockBean
    private lateinit var securityPort: NoticeSecurityPort

    @MockBean
    private lateinit var commentNoticePort: CommandNoticePort

    private lateinit var postNoticeUseCase: PostNoticeUseCase

    @BeforeEach
    fun setUp() {
        postNoticeUseCase = PostNoticeUseCase(
            securityPort, commentNoticePort
        )
    }

    private val managerId = UUID.randomUUID()
    private val title = "wpahr"
    private val content = "sodyd"

    @Test
    fun `공지사항 생성 성공`() {
        //given
        given(securityPort.getCurrentUserId())
            .willReturn(managerId)

        // when & then
        assertDoesNotThrow {
            postNoticeUseCase.execute(title, content)
        }
    }
}