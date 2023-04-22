package team.aliens.dms.domain.notice.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.notice.dto.QueryAllNoticesResponse
import team.aliens.dms.domain.notice.dto.QueryAllNoticesResponse.NoticeDetails
import team.aliens.dms.domain.notice.model.OrderType
import team.aliens.dms.domain.notice.service.NoticeService
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class QueryAllNoticesUseCase(
    private val userService: UserService,
    private val noticeService: NoticeService
) {

    fun execute(orderType: String): QueryAllNoticesResponse {
        val user = userService.getCurrentUser()
        val order = OrderType.valueOf(orderType)
        val notices = noticeService.getAllNoticesBySchoolIdAndOrder(user.schoolId, order)

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
