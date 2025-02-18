package team.aliens.dms.domain.vote.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.common.spi.NotificationEventPort
import team.aliens.dms.common.spi.TaskSchedulerPort
import team.aliens.dms.domain.notice.model.Notice
import team.aliens.dms.domain.notice.spi.CommandNoticePort
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.model.Notification
import team.aliens.dms.domain.notification.spi.QueryDeviceTokenPort
import team.aliens.dms.domain.vote.dto.VoteResultNoticeInfo
import java.time.LocalDateTime

@Service
class VoteResultNoticeSchedulerServiceImpl(
    private val taskSchedulerPort: TaskSchedulerPort,
    private val commandNoticePort: CommandNoticePort,
    private val notificationEventPort: NotificationEventPort,
    private val deviceTokenPort: QueryDeviceTokenPort,
) : VoteResultNoticeSchedulerService {

    override fun scheduleVoteResultNotice(voteResultNoticeInfo: VoteResultNoticeInfo) {

        taskSchedulerPort.scheduleTask(
            voteResultNoticeInfo.savedVotingTopicId, {
                val deviceTokens: List<DeviceToken> = deviceTokenPort.queryDeviceTokensBySchoolId(voteResultNoticeInfo.schoolId)
                commandNoticePort.saveNotice(
                    Notice(
                        title = voteResultNoticeInfo.title,
                        content = voteResultNoticeInfo.content,
                        managerId = voteResultNoticeInfo.managerId,
                        createdAt = LocalDateTime.now(),
                        updatedAt = LocalDateTime.now()
                    )
                ).also {
                    notificationEventPort.publishNotificationToApplicant(
                        deviceTokens, Notification.NoticeNotification(voteResultNoticeInfo.schoolId, it)
                    )
                }
            }, voteResultNoticeInfo.reservedTime
        )
    }
}
