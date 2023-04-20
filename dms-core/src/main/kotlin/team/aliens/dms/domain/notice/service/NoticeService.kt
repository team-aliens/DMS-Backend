package team.aliens.dms.domain.notice.service

class NoticeService(
    queryNoticeService: QueryNoticeService,
    checkNoticeService: CheckNoticeService,
    commandNoticeService: CommandNoticeService
) : QueryNoticeService by queryNoticeService,
    CheckNoticeService by checkNoticeService,
    CommandNoticeService by commandNoticeService