package team.aliens.dms.domain.notice.usecase

import java.time.LocalDateTime
import java.util.UUID
import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.notice.model.Notice
import team.aliens.dms.domain.notice.spi.CommandNoticePort
import team.aliens.dms.domain.user.service.GetUserService

@UseCase
class CreateNoticeUseCase(
    private val getUserService: GetUserService,
    private val commentNoticePort: CommandNoticePort
) {

    fun execute(title: String, content: String): UUID {
        val user = getUserService.getCurrentUser()

        val notice = Notice(
            managerId = user.id,
            title = title,
            content = content,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        val savedNotice = commentNoticePort.saveNotice(notice)

        return savedNotice.id
    }
}
