package team.aliens.dms.domain.notice.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.notice.dto.NoticeIdResponse
import team.aliens.dms.domain.notice.model.Notice
import team.aliens.dms.domain.notice.service.NoticeService
import team.aliens.dms.domain.user.service.UserService
import java.time.LocalDateTime
import java.util.UUID

@UseCase
class CreateNoticeUseCase(
    private val noticeService: NoticeService,
    private val securityService: SecurityService
) {

    fun execute(title: String, content: String): NoticeIdResponse {

        val managerId = securityService.getCurrentUserId()

        val notice = Notice(
            managerId = managerId,
            title = title,
            content = content,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        val savedNotice = noticeService.saveNotice(notice)

        return NoticeIdResponse(savedNotice.id)
    }
}
