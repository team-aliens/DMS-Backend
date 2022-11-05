package team.aliens.dms.domain.notice.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.notice.model.Notice
import team.aliens.dms.domain.notice.spi.CommandNoticePort
import team.aliens.dms.domain.notice.spi.NoticeSecurityPort
import java.time.LocalDateTime

@UseCase
class PostNoticeUseCase(
    private val securityPort: NoticeSecurityPort,
    private val commentNoticePort: CommandNoticePort
) {

    fun execute(title: String, content: String) {
        val managerId = securityPort.getCurrentUserId()

        val notice = Notice(
            managerId = managerId,
            title = title,
            content = content,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        commentNoticePort.saveNotice(notice)
    }
}