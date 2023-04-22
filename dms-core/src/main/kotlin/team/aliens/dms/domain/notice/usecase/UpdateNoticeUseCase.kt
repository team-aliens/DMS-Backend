package team.aliens.dms.domain.notice.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.notice.service.NoticeService
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@UseCase
class UpdateNoticeUseCase(
    private val userService: UserService,
    private val noticeService: NoticeService
) {

    fun execute(noticeId: UUID, title: String, content: String): UUID {
        val user = userService.getCurrentUser()
        val notice = noticeService.getNoticeByIdAndManagerId(noticeId, user.id)

        noticeService.saveNotice(
            notice.copy(
                title = title,
                content = content
            )
        )

        return notice.id
    }
}
