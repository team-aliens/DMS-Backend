package team.aliens.dms.domain.notice.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.notice.exception.IsNotWriterException
import team.aliens.dms.domain.notice.exception.NoticeNotFoundException
import team.aliens.dms.domain.notice.spi.CommandNoticePort
import team.aliens.dms.domain.notice.spi.NoticeSecurityPort
import team.aliens.dms.domain.notice.spi.QueryNoticePort
import java.util.UUID

@UseCase
class RemoveNoticeUseCase(
    private val queryNoticePort: QueryNoticePort,
    private val commandNoticePort: CommandNoticePort,
    private val securityPort: NoticeSecurityPort
) {

    fun execute(noticeId: UUID) {
        val currentManagerId = securityPort.getCurrentUserId()
        val notice = queryNoticePort.queryNoticeById(noticeId) ?: throw NoticeNotFoundException

        if (notice.managerId != currentManagerId) {
            throw IsNotWriterException
        }

        commandNoticePort.deleteNotice(notice)
    }
}
