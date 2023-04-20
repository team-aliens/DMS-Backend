package team.aliens.dms.domain.notice.service

import team.aliens.dms.common.annotation.Service

@Service
class NoticeService(
    queryNoticeService: QueryNoticeService,
    checkNoticeService: CheckNoticeService,
    commandNoticeService: CommandNoticeService
) : QueryNoticeService by queryNoticeService,
    CheckNoticeService by checkNoticeService,
    CommandNoticeService by commandNoticeService
