package team.aliens.dms.domain.notice.usecase

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import team.aliens.dms.domain.notice.exception.IsNotWriterException
import team.aliens.dms.domain.notice.exception.NoticeNotFoundException
import team.aliens.dms.domain.notice.model.Notice
import team.aliens.dms.domain.notice.spi.CommandNoticePort
import team.aliens.dms.domain.notice.spi.NoticeSecurityPort
import team.aliens.dms.domain.notice.spi.QueryNoticePort
import java.time.LocalDateTime
import java.util.UUID

@ExtendWith(SpringExtension::class)
class RemoveNoticeUseCaseTests {

    @MockBean
    private lateinit var queryNoticePort: QueryNoticePort

    @MockBean
    private lateinit var commandNoticePort: CommandNoticePort

    @MockBean
    private lateinit var securityPort: NoticeSecurityPort

    private lateinit var removeNoticeUseCase: RemoveNoticeUseCase

    @BeforeEach
    fun setUp() {
        removeNoticeUseCase = RemoveNoticeUseCase(
            queryNoticePort,
            commandNoticePort,
            securityPort
        )
    }

    private val managerId = UUID.randomUUID()
    private val noticeId = UUID.randomUUID()

    private val noticeStub by lazy {
        Notice(
            id = noticeId,
            managerId = managerId,
            title = "test title",
            content = "test content",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
    }

    @Test
    fun `공지사항 삭제 성공`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(managerId)

        given(queryNoticePort.queryNoticeById(noticeId))
            .willReturn(noticeStub)

        // when & then
        assertDoesNotThrow {
            removeNoticeUseCase.execute(noticeId)
        }
    }

    @Test
    fun `공지사항이 존재하지 않음`() {
        // given
        given(securityPort.getCurrentUserId())
            .willReturn(managerId)

        given(queryNoticePort.queryNoticeById(noticeId))
            .willReturn(null)

        // when & then
        assertThrows<NoticeNotFoundException> {
            removeNoticeUseCase.execute(noticeId)
        }
    }

    @Test
    fun `작성자가 아니라 삭제할 수 없음`() {
        val otherUserId = UUID.randomUUID()

        // given
        given(securityPort.getCurrentUserId())
            .willReturn(otherUserId)

        given(queryNoticePort.queryNoticeById(noticeId))
            .willReturn(noticeStub)

        // when & then
        assertThrows<IsNotWriterException> {
            removeNoticeUseCase.execute(noticeId)
        }
    }
}