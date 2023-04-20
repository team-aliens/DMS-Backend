package team.aliens.dms.domain.notice.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.notice.dto.QueryNoticeDetailsResponse
import team.aliens.dms.domain.notice.exception.NoticeNotFoundException
import team.aliens.dms.domain.notice.spi.QueryNoticePort
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID
import team.aliens.dms.domain.notice.service.NoticeService
import team.aliens.dms.domain.notice.service.QueryNoticeService

@ReadOnlyUseCase
class QueryNoticeDetailsUseCase(
    private val userService: UserService,
    private val noticeService: NoticeService
) {

    fun execute(noticeId: UUID): QueryNoticeDetailsResponse {
        val user = userService.getCurrentUser()
        val notice = noticeService.getNoticeById(noticeId)

        val writer = userService.queryUserById(notice.managerId)
        validateSameSchool(writer.schoolId, user.schoolId)

        return QueryNoticeDetailsResponse(
            id = notice.id,
            title = notice.title,
            content = notice.content,
            createdAt = notice.createdAt!!
        )
    }
}
