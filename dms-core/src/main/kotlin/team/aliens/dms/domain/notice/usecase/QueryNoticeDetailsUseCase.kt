package team.aliens.dms.domain.notice.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.notice.dto.QueryNoticeDetailsResponse
import team.aliens.dms.domain.notice.exception.NoticeNotFoundException
import team.aliens.dms.domain.notice.spi.QueryNoticePort
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@ReadOnlyUseCase
class QueryNoticeDetailsUseCase(
    private val userService: UserService,
    private val queryNoticePort: QueryNoticePort
) {

    fun execute(noticeId: UUID): QueryNoticeDetailsResponse {
        val user = userService.getCurrentUser()
        val notice = queryNoticePort.queryNoticeById(noticeId) ?: throw NoticeNotFoundException

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
