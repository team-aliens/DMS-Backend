package team.aliens.dms.domain.notice.service

import team.aliens.dms.domain.notice.model.Notice

interface CommandNoticeService {

    fun saveNotice(notice: Notice): Notice

    fun deleteNotice(notice: Notice)
}
