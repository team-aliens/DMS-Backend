package team.aliens.dms.domain.notice.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.notice.dto.LatestNoticeResponse
import team.aliens.dms.domain.notice.service.NoticeService
import team.aliens.dms.domain.user.service.UserService

@ReadOnlyUseCase
class QueryLatestNoticeUseCase(
    private val userService: UserService,
    private val noticeService: NoticeService
) {

    fun execute(): LatestNoticeResponse? {
        val user = userService.getCurrentUser()
        val latestNotice = noticeService.getLatestNoticeBySchoolId(user.schoolId)

        return latestNotice?.let { LatestNoticeResponse.of(it) }
    }
}
