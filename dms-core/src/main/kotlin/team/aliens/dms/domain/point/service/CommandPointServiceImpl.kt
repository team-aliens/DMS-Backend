package team.aliens.dms.domain.point.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.common.spi.NotificationEventPort
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.model.Notification
import team.aliens.dms.domain.notification.spi.QueryDeviceTokenPort
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.point.model.PointOption
import team.aliens.dms.domain.point.spi.CommandPointHistoryPort
import team.aliens.dms.domain.point.spi.CommandPointOptionPort
import team.aliens.dms.domain.student.spi.QueryStudentPort
import java.util.UUID

@Service
class CommandPointServiceImpl(
    private val commandPointHistoryPort: CommandPointHistoryPort,
    private val commandPointOptionPort: CommandPointOptionPort,
    private val notificationEventPort: NotificationEventPort,
    private val securityPort: SecurityPort,
    private val deviceTokenPort: QueryDeviceTokenPort,
    private val queryStudentPort: QueryStudentPort
) : CommandPointService {

    override fun savePointHistory(pointHistory: PointHistory) =
        commandPointHistoryPort.savePointHistory(pointHistory)

    override fun deletePointHistory(pointHistory: PointHistory) {
        commandPointHistoryPort.deletePointHistory(pointHistory)
    }

    override fun saveAllPointHistories(pointHistories: List<PointHistory>, studentIds: List<UUID>?) {
        studentIds?.let {
            val deviceTokens: List<DeviceToken> = deviceTokenPort.queryDeviceTokensByStudentIds(studentIds)

            notificationEventPort.publishNotificationToApplicant(
                deviceTokens, Notification.PointNotification(pointHistories.first())
            )
        }

        commandPointHistoryPort.saveAllPointHistories(pointHistories)
    }

    override fun savePointOption(pointOption: PointOption): PointOption =
        commandPointOptionPort.savePointOption(pointOption)

    override fun deletePointOption(pointOption: PointOption) {
        commandPointOptionPort.deletePointOption(pointOption)
    }
}
