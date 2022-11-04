package team.aliens.dms.domain.notice.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.notice.dto.QueryAllNoticesResponse
import team.aliens.dms.domain.notice.dto.QueryAllNoticesResponse.NoticeDetails
import team.aliens.dms.domain.notice.model.OrderType
import team.aliens.dms.domain.notice.spi.NoticeQueryUserPort
import team.aliens.dms.domain.notice.spi.NoticeSecurityPort
import team.aliens.dms.domain.notice.spi.QueryNoticePort
import team.aliens.dms.domain.user.exception.UserNotFoundException

@ReadOnlyUseCase
class QueryAllNoticesUseCase(
    private val securityPort: NoticeSecurityPort,
    private val queryUserPort: NoticeQueryUserPort,
    private val queryNoticePort: QueryNoticePort
) {

    fun execute(orderType: String): QueryAllNoticesResponse {
        val currentUserId = securityPort.getCurrentUserId()
        val user = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val order = OrderType.valueOf(orderType)
        val notices = queryNoticePort.queryAllNoticesBySchoolIdOrder(user.schoolId, order)

        return QueryAllNoticesResponse(
            notices.map {
                NoticeDetails(
                    id = it.id,
                    title = it.title,
                    createdAt = it.createdAt!!
                )
            }
        )
    }
}