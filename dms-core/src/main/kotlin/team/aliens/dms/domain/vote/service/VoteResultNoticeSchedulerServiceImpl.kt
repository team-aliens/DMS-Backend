package team.aliens.dms.domain.vote.service

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
import java.util.*

@Service
class VoteResultNoticeSchedulerServiceImpl(
    private val taskSchedulerPort: TaskSchedulerPort,
    private val commandNoticePort: CommandNoticePort,
    private val notificationEventPort: NotificationEventPort,
    private val deviceTokenPort: QueryDeviceTokenPort,
    private val securityPort: SecurityPort
) : VoteResultNoticeSchedulerService {

    override fun scheduleVoteResultNotice(savedVotingTopicId: UUID, reservedTime: LocalDateTime) {

        val managerId = securityPort.getCurrentUserId()
        val schoolId = securityPort.getCurrentUserSchoolId()

        taskSchedulerPort.scheduleTask(
            savedVotingTopicId, {
                val deviceTokens: List<DeviceToken> = deviceTokenPort.queryDeviceTokensBySchoolId(schoolId)
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
