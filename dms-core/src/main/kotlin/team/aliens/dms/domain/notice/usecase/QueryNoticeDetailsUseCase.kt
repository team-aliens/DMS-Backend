package team.aliens.dms.domain.notice.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.notice.dto.QueryNoticeDetailsResponse
import team.aliens.dms.domain.notice.exception.NoticeNotFoundException
import team.aliens.dms.domain.notice.spi.QueryNoticePort
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.spi.QueryUserPort
import java.util.UUID

@ReadOnlyUseCase
class QueryNoticeDetailsUseCase(
    private val securityPort: SecurityPort,
    private val queryNoticePort: QueryNoticePort,
    private val queryUserPort: QueryUserPort
) {

    fun execute(noticeId: UUID): QueryNoticeDetailsResponse {
        val currentUserId = securityPort.getCurrentUserId()
        val notice = queryNoticePort.queryNoticeById(noticeId) ?: throw NoticeNotFoundException

        val writer = queryUserPort.queryUserById(notice.managerId) ?: throw ManagerNotFoundException
        val viewer = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        validateSameSchool(writer.schoolId, viewer.schoolId)

        return QueryNoticeDetailsResponse(
            id = notice.id,
            title = notice.title,
            content = notice.content,
            createdAt = notice.createdAt!!
        )
    }
}
