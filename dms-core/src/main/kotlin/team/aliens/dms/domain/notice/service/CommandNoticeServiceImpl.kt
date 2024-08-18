package team.aliens.dms.domain.notice.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.common.spi.NotificationEventPort
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.notice.model.Notice
import team.aliens.dms.domain.notice.spi.CommandNoticePort
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.model.Notification
import team.aliens.dms.domain.notification.spi.QueryDeviceTokenPort

@Service
class CommandNoticeServiceImpl(
        private val commandNoticePort: CommandNoticePort,
        private val notificationEventPort: NotificationEventPort,
        private val securityPort: SecurityPort,
        private val deviceTokenPort: QueryDeviceTokenPort,
        ) : CommandNoticeService {

    override fun saveNotice(notice: Notice): Notice {
        val schoolId = securityPort.getCurrentUserSchoolId()
        val deviceTokens: List<DeviceToken> = deviceTokenPort.queryDeviceTokensBySchoolId(schoolId)

        return commandNoticePort.saveNotice(notice)
            .also {
                notificationEventPort.publishNotificationToAllByTopic(
                    deviceTokens, Notification.NoticeNotification(schoolId, it)
                )
            }
    }

    override fun deleteNotice(notice: Notice) {
        commandNoticePort.deleteNotice(notice)
    }
}
