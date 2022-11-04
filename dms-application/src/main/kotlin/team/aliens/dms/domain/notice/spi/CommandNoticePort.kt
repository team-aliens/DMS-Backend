package team.aliens.dms.domain.notice.spi

import team.aliens.dms.domain.notice.model.Notice

interface CommandNoticePort {

    fun deleteNotice(notice: Notice)

}