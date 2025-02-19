package team.aliens.dms.domain.notice.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.common.spi.NotificationEventPort
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.common.spi.TaskSchedulerPort
import team.aliens.dms.domain.notice.model.Notice
import team.aliens.dms.domain.notice.spi.CommandNoticePort
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.model.Notification
import team.aliens.dms.domain.notification.spi.QueryDeviceTokenPort
import java.time.LocalDateTime
import java.util.UUID

@Service
class CommandNoticeServiceImpl(
    private val commandNoticePort: CommandNoticePort,
    private val notificationEventPort: NotificationEventPort,
    private val securityPort: SecurityPort,
    private val deviceTokenPort: QueryDeviceTokenPort,
    private val taskSchedulerPort: TaskSchedulerPort
) : CommandNoticeService {

    override fun saveNotice(notice: Notice): Notice {
        val schoolId = securityPort.getCurrentUserSchoolId()
        val deviceTokens: List<DeviceToken> = deviceTokenPort.queryDeviceTokensBySchoolId(schoolId)

        return commandNoticePort.saveNotice(notice)
            .also {
                notificationEventPort.publishNotificationToApplicant(
                    deviceTokens, Notification.NoticeNotification(schoolId, it)
                )
            }
    }

    override fun deleteNotice(notice: Notice) {
        commandNoticePort.deleteNotice(notice)
    }

    override fun scheduleVoteResultNotice(savedVotingTopicId: UUID, reservedTime: LocalDateTime) {

        val managerId = securityPort.getCurrentUserId()
        val schoolId = securityPort.getCurrentUserSchoolId()
        val deviceTokens: List<DeviceToken> = deviceTokenPort.queryDeviceTokensBySchoolId(schoolId)

        taskSchedulerPort.scheduleTask(
            savedVotingTopicId, {
                commandNoticePort.saveNotice(
                    Notice(
                        title = "임시",
                        content = "임시",
                        managerId = managerId,
                        createdAt = LocalDateTime.now(),
                        updatedAt = LocalDateTime.now()
                    )
                ).also {
                    notificationEventPort.publishNotificationToApplicant(
                        deviceTokens, Notification.NoticeNotification(schoolId, it)
                    )
                }
            }, reservedTime
        )
    }
}
