package team.aliens.dms.domain.notice.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.notice.dto.QueryNoticeDetailsResponse
import team.aliens.dms.domain.notice.exception.NoticeNotFoundException
import team.aliens.dms.domain.notice.spi.NoticeQueryUserPort
import team.aliens.dms.domain.notice.spi.NoticeSecurityPort
import team.aliens.dms.domain.notice.spi.QueryNoticePort
import team.aliens.dms.domain.school.exception.SchoolInfoMismatchException
import team.aliens.dms.domain.user.exception.UserNotFoundException
import java.util.UUID

@ReadOnlyUseCase
class QueryNoticeDetailsUseCase(
    private val securityPort: NoticeSecurityPort,
    private val queryNoticePort: QueryNoticePort,
    private val queryUserPort: NoticeQueryUserPort
) {

    fun execute(noticeId: UUID): QueryNoticeDetailsResponse {
        val currentUserId = securityPort.getCurrentUserId()
        val notice = queryNoticePort.queryNoticeById(noticeId) ?: throw NoticeNotFoundException

        val writer = queryUserPort.queryUserById(notice.managerId) ?: throw UserNotFoundException
        val viewer = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        if (writer.schoolId != viewer.schoolId) {
            throw SchoolInfoMismatchException
        }

        return QueryNoticeDetailsResponse(
            title = notice.title,
            content = notice.content,
            createdAt = notice.createdAt!!
        )
    }
}