package team.aliens.dms.domain.notice.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.notice.exception.NoticeNotFoundException
import team.aliens.dms.domain.notice.spi.CommandNoticePort
import team.aliens.dms.domain.notice.spi.NoticeSecurityPort
import team.aliens.dms.domain.notice.spi.QueryNoticePort
import java.util.UUID

@UseCase
class UpdateNoticeUseCase(
    private val queryNoticePort: QueryNoticePort,
    private val commandNoticePort: CommandNoticePort,
    private val securityPort: NoticeSecurityPort
) {

    fun execute(noticeId: UUID, title: String, content: String) {
        val managerId = securityPort.getCurrentUserId()
        val notice = queryNoticePort.queryNoticeByIdAndManagerId(noticeId, managerId) ?: throw NoticeNotFoundException

        commandNoticePort.saveNotice(
            notice.copy(
                title = title,
                content = content
            )
        )
    }
}