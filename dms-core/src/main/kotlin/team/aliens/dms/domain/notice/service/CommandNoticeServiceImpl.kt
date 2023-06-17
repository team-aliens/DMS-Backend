package team.aliens.dms.domain.notice.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.common.spi.NotificationEventPort
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.notice.model.Notice
import team.aliens.dms.domain.notice.spi.CommandNoticePort
import team.aliens.dms.domain.notification.model.Notification

@Service
class CommandNoticeServiceImpl(
    private val commandNoticePort: CommandNoticePort,
    private val notificationEventPort: NotificationEventPort,
    private val securityPort: SecurityPort
) : CommandNoticeService {

    override fun saveNotice(notice: Notice): Notice {
        val schoolId = securityPort.getCurrentUserSchoolId()
        return commandNoticePort.saveNotice(notice)
            .also {
                notificationEventPort.publishNotificationToAllByTopic(
                    Notification.NoticeNotification(schoolId, it)
                )
            }
    }

    override fun deleteNotice(notice: Notice) {
        commandNoticePort.deleteNotice(notice)
    }
}
