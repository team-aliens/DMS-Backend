package team.aliens.dms.domain.notice.usecase

import team.aliens.dms.common.annotation.SchedulerUseCase
import team.aliens.dms.domain.notice.dto.VoteResultNoticeRequest
import team.aliens.dms.domain.notice.service.NoticeService

@SchedulerUseCase
class VoteResultNoticeUseCase(
    private val noticeService: NoticeService
) {
    fun execute(voteResultNoticeRequest: VoteResultNoticeRequest) {
        noticeService.voteResultNotice(
            voteResultNoticeRequest.votingTopicId,
            voteResultNoticeRequest.reservedTime,
            voteResultNoticeRequest.isReNotice
        )
    }
}
