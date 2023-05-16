package team.aliens.dms.domain.notice.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.notice.dto.NoticeResponse
import team.aliens.dms.domain.notice.dto.NoticesResponse
import team.aliens.dms.domain.notice.model.OrderType
import team.aliens.dms.domain.notice.service.NoticeService
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class QueryAllNoticesUseCase(
    private val userService: UserService,
    private val noticeService: NoticeService
) {

    fun execute(orderType: String): NoticesResponse {
        val user = userService.getCurrentUser()
        val order = OrderType.valueOf(orderType)
        val notices = noticeService.getAllNoticesBySchoolIdAndOrder(user.schoolId, order)

        return NoticesResponse(
            notices.map {
                NoticeResponse.of(it)
            }
        )
    }
}
