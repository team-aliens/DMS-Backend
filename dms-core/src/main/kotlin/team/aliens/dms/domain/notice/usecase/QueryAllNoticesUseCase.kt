package team.aliens.dms.domain.notice.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.notice.dto.QueryAllNoticesResponse
import team.aliens.dms.domain.notice.dto.QueryAllNoticesResponse.NoticeDetails
import team.aliens.dms.domain.notice.model.OrderType
import team.aliens.dms.domain.notice.spi.QueryNoticePort
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.spi.QueryUserPort

@ReadOnlyUseCase
class QueryAllNoticesUseCase(
    private val securityPort: SecurityPort,
    private val queryUserPort: QueryUserPort,
    private val queryNoticePort: QueryNoticePort
) {

    fun execute(orderType: String): QueryAllNoticesResponse {
        val currentUserId = securityPort.getCurrentUserId()
        val user = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val order = OrderType.valueOf(orderType)
        val notices = queryNoticePort.queryAllNoticesBySchoolIdAndOrder(user.schoolId, order)

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
