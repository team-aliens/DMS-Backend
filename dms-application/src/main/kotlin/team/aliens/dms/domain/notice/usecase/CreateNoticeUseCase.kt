package team.aliens.dms.domain.notice.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.notice.model.Notice
import team.aliens.dms.domain.notice.spi.CommandNoticePort
import team.aliens.dms.domain.notice.spi.NoticeSecurityPort
import java.time.LocalDateTime

@UseCase
class CreateNoticeUseCase(
    private val securityPort: NoticeSecurityPort,
    private val commentNoticePort: CommandNoticePort
) {

    fun execute(title: String, content: String) {
        val currentManagerId = securityPort.getCurrentUserId()

        val notice = Notice(
            managerId = currentManagerId,
            title = title,
            content = content,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        commentNoticePort.saveNotice(notice)
    }
}