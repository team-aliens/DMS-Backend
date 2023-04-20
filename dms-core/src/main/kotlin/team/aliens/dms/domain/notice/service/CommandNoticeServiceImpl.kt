package team.aliens.dms.domain.notice.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.notice.model.Notice
import team.aliens.dms.domain.notice.spi.CommandNoticePort

@Service
class CommandNoticeServiceImpl(
    private val commandNoticePort: CommandNoticePort
) : CommandNoticeService {

    override fun saveNotice(notice: Notice): Notice {
        return commandNoticePort.saveNotice(notice)
    }

    override fun deleteNotice(notice: Notice) {
        commandNoticePort.deleteNotice(notice)
    }
}
