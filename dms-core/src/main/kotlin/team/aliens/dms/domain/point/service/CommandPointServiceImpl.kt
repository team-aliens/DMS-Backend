package team.aliens.dms.domain.point.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.common.spi.NotificationEventPort
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.notification.exception.DeviceTokenNotFoundException
import team.aliens.dms.domain.notification.model.Notification
import team.aliens.dms.domain.notification.spi.QueryDeviceTokenPort
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.point.model.PointOption
import team.aliens.dms.domain.point.spi.CommandPointHistoryPort
import team.aliens.dms.domain.point.spi.CommandPointOptionPort

@Service
class CommandPointServiceImpl(
    private val commandPointHistoryPort: CommandPointHistoryPort,
    private val commandPointOptionPort: CommandPointOptionPort,
    private val notificationEventPort: NotificationEventPort,
    private val securityPort: SecurityPort,
    private val deviceTokenPort: QueryDeviceTokenPort
) : CommandPointService {

    override fun savePointHistory(pointHistory: PointHistory) =
        commandPointHistoryPort.savePointHistory(pointHistory)

    override fun deletePointHistory(pointHistory: PointHistory) {
        commandPointHistoryPort.deletePointHistory(pointHistory)
    }

    override fun saveAllPointHistories(pointHistories: List<PointHistory>) {
        val userId = securityPort.getCurrentUserId()
        val deviceToken = deviceTokenPort.queryDeviceTokenByUserId(userId)
            ?: throw DeviceTokenNotFoundException

        commandPointHistoryPort.saveAllPointHistories(pointHistories)

        pointHistories.forEach { pointHistory ->
            notificationEventPort.publishNotification(
                deviceToken,
                Notification.PointNotification(pointHistory)
            )
        }
    }

    override fun savePointOption(pointOption: PointOption): PointOption =
        commandPointOptionPort.savePointOption(pointOption)

    override fun deletePointOption(pointOption: PointOption) {
        commandPointOptionPort.deletePointOption(pointOption)
    }
}
