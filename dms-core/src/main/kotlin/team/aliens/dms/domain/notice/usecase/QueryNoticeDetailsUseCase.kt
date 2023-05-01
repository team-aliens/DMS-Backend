package team.aliens.dms.domain.notice.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.notice.dto.QueryNoticeDetailsResponse
import team.aliens.dms.domain.notice.service.NoticeService
import team.aliens.dms.domain.school.exception.SchoolMismatchException
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@ReadOnlyUseCase
class QueryNoticeDetailsUseCase(
    private val userService: UserService,
    private val noticeService: NoticeService
) {

    fun execute(noticeId: UUID): QueryNoticeDetailsResponse {
        val user = userService.getCurrentUser()
        val notice = noticeService.getNoticeById(noticeId)

        userService.queryUserById(notice.managerId).let { writer ->
            if (user.schoolId != writer.schoolId) {
                throw SchoolMismatchException
            }
        }

        return QueryNoticeDetailsResponse(
            id = notice.id,
            title = notice.title,
            content = notice.content,
            createdAt = notice.createdAt!!
        )
    }
}
