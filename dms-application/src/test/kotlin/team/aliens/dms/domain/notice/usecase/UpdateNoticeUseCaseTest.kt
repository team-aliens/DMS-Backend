package team.aliens.dms.domain.notice.usecase

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.notice.exception.NoticeNotFoundException
import team.aliens.dms.domain.notice.spi.CommandNoticePort
import team.aliens.dms.domain.notice.spi.NoticeSecurityPort
import team.aliens.dms.domain.notice.spi.QueryNoticePort
import team.aliens.dms.domain.notice.stub.createNoticeStub
import java.util.UUID

@ExtendWith(SpringExtension::class)
class UpdateNoticeUseCaseTest {

    @MockBean
    private lateinit var queryNoticePort: QueryNoticePort

    @MockBean
    private lateinit var commandNoticePort: CommandNoticePort

    @MockBean
    private lateinit var securityPort: NoticeSecurityPort

    private lateinit var updateNoticeUseCase: UpdateNoticeUseCase

    @BeforeEach
    fun setUp() {
        updateNoticeUseCase = UpdateNoticeUseCase(
            queryNoticePort, commandNoticePort, securityPort
        )
    }

    private val managerId = UUID.randomUUID()
    private val noticeId = UUID.randomUUID()
    private val title = "title"
    private val content = "content"

    private val noticeStub by lazy {
        createNoticeStub(id = noticeId, title = title, content = content)
    }

    @Test
    fun `공지사항 수정 성공`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(managerId)

        given(queryNoticePort.queryNoticeByIdAndManagerId(noticeId, managerId))
            .willReturn(noticeStub)

        // when
        val response = updateNoticeUseCase.execute(noticeId, title, content)

        // then
        assertThat(response).isEqualTo(noticeId)
    }

    @Test
    fun `공지사항 존재하지 않음`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(managerId)

        given(queryNoticePort.queryNoticeByIdAndManagerId(noticeId, managerId))
            .willReturn(null)

        // when & then
        assertThrows<NoticeNotFoundException> {
            updateNoticeUseCase.execute(noticeId, title, content)
        }
    }
}
