package team.aliens.dms.domain.notice.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.notice.exception.IsNotWriterException
import team.aliens.dms.domain.notice.exception.NoticeNotFoundException
import team.aliens.dms.domain.notice.spi.CommandNoticePort
import team.aliens.dms.domain.notice.spi.QueryNoticePort
import team.aliens.dms.domain.user.service.GetUserService
import java.util.UUID

@UseCase
class RemoveNoticeUseCase(
    private val getUserService: GetUserService,
    private val queryNoticePort: QueryNoticePort,
    private val commandNoticePort: CommandNoticePort
) {

    fun execute(noticeId: UUID) {
        val user = getUserService.getCurrentUser()
        val notice = queryNoticePort.queryNoticeById(noticeId) ?: throw NoticeNotFoundException

        if (notice.managerId != user.id) {
            throw IsNotWriterException
        }

        commandNoticePort.deleteNotice(notice)
    }
}
