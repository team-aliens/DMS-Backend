package team.aliens.dms.domain.vote.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.common.spi.NotificationEventPort
import team.aliens.dms.common.spi.TaskSchedulerPort
import team.aliens.dms.domain.notice.model.Notice
import team.aliens.dms.domain.notice.spi.CommandNoticePort
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.model.Notification
import team.aliens.dms.domain.notification.spi.QueryDeviceTokenPort
import java.time.LocalDateTime
import java.util.*

@Service
class ScheduleCreateVoteResultNoticeServiceImpl(
    private val taskSchedulerPort: TaskSchedulerPort,
    private val commandNoticePort: CommandNoticePort,
    private val notificationEventPort: NotificationEventPort,
    private val deviceTokenPort: QueryDeviceTokenPort,
): ScheduleCreateVoteResultNoticeService {
    override fun exectue(endTime: LocalDateTime, managerId: UUID,schoolId: UUID, title: String, content: String) {

         taskSchedulerPort.schduleTask({

                    val deviceTokens: List<DeviceToken> = deviceTokenPort.queryDeviceTokensBySchoolId(schoolId)
                     commandNoticePort.saveNotice(Notice(
                        title = title,
                        content = content,
                        managerId = managerId,
                        createdAt = LocalDateTime.now(),
                        updatedAt = LocalDateTime.now()
                    )).also {
                            notificationEventPort.publishNotificationToApplicant(
                                deviceTokens, Notification.NoticeNotification(schoolId, it)
                            )}

                }
          ,endTime
        )
    }
}