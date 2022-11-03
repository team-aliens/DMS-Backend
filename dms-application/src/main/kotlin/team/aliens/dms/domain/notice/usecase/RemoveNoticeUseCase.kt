package team.aliens.dms.domain.notice.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.notice.exception.NoticeNotFoundException
import team.aliens.dms.domain.notice.spi.CommandNoticePort
import team.aliens.dms.domain.notice.spi.QueryNoticePort
import java.util.UUID

@UseCase
class RemoveNoticeUseCase(
    private val queryNoticePort: QueryNoticePort,
    private val commandNoticePort: CommandNoticePort
) {

    fun execute(noticeId: UUID) {
        val notice = queryNoticePort.queryNoticeById(noticeId) ?: throw NoticeNotFoundException

        commandNoticePort.deleteNotice(notice)
    }
}