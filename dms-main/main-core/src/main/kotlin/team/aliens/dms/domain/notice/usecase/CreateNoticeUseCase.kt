package team.aliens.dms.domain.notice.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.notice.dto.NoticeIdResponse
import team.aliens.dms.domain.notice.model.Notice
import team.aliens.dms.domain.notice.service.NoticeService
import team.aliens.dms.domain.user.service.UserService
import java.time.LocalDateTime
import java.util.UUID

@UseCase
class CreateNoticeUseCase(
    private val noticeService: NoticeService,
    private val userService: UserService
) {

    fun execute(title: String, content: String): NoticeIdResponse {

        val firstUuid = UUID.fromString("33386262-6332-6663-2d37-6664312d3131")

        val notice = Notice(
            managerId = firstUuid,
            title = title,
            content = content,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        val savedNotice = noticeService.saveNotice(notice)

        return NoticeIdResponse(savedNotice.id)
    }
}
