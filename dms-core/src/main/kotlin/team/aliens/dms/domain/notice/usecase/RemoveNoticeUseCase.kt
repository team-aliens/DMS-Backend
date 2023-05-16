package team.aliens.dms.domain.notice.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.notice.exception.IsNotWriterException
import team.aliens.dms.domain.notice.service.NoticeService
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@UseCase
class RemoveNoticeUseCase(
    private val userService: UserService,
    private val noticeService: NoticeService
) {

    fun execute(noticeId: UUID) {
        val user = userService.getCurrentUser()
        val notice = noticeService.getNoticeById(noticeId)

        if (notice.managerId != user.id) {
            throw IsNotWriterException
        }

        noticeService.deleteNotice(notice)
    }
}
