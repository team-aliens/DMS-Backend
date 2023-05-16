package team.aliens.dms.domain.notice.service

import team.aliens.dms.common.annotation.Service

@Service
class NoticeService(
    getNoticeService: GetNoticeService,
    checkNoticeService: CheckNoticeService,
    commandNoticeService: CommandNoticeService
) : GetNoticeService by getNoticeService,
    CheckNoticeService by checkNoticeService,
    CommandNoticeService by commandNoticeService
