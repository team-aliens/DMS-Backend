package team.aliens.dms.domain.notice.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.common.spi.NotificationEventPort
import team.aliens.dms.domain.notice.model.Notice
import team.aliens.dms.domain.notice.spi.CommandNoticePort
import team.aliens.dms.domain.notification.model.Notification

@Service
class CommandNoticeServiceImpl(
    private val commandNoticePort: CommandNoticePort,
    private val notificationEventPort: NotificationEventPort
) : CommandNoticeService {

    override fun saveNotice(notice: Notice): Notice {
        notificationEventPort.publishNotificationToAllByTopic(
            Notification.NoticeNotification(notice)
        )
        return commandNoticePort.saveNotice(notice)
    }

    override fun deleteNotice(notice: Notice) {
        commandNoticePort.deleteNotice(notice)
    }
}
