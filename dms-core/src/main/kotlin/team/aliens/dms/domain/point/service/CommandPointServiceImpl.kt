package team.aliens.dms.domain.point.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.common.spi.NotificationEventPort
import team.aliens.dms.domain.notification.model.Notification
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.point.model.PointOption
import team.aliens.dms.domain.point.spi.CommandPointHistoryPort
import team.aliens.dms.domain.point.spi.CommandPointOptionPort

@Service
class CommandPointServiceImpl(
    private val commandPointHistoryPort: CommandPointHistoryPort,
    private val commandPointOptionPort: CommandPointOptionPort,
    private val notificationEventPort: NotificationEventPort,
) : CommandPointService {

    override fun savePointHistory(pointHistory: PointHistory) =
        commandPointHistoryPort.savePointHistory(pointHistory)

    override fun deletePointHistory(pointHistory: PointHistory) {
        commandPointHistoryPort.deletePointHistory(pointHistory)
    }

    override fun saveAllPointHistories(pointHistories: List<PointHistory>) {
        commandPointHistoryPort.saveAllPointHistories(pointHistories)
    }

    override fun savePointOption(pointOption: PointOption): PointOption {
        return commandPointOptionPort.savePointOption(pointOption)
            .also {
                notificationEventPort.publishNotificationToAllByTopic(
                    Notification.PointNotification(it)
                )
            }
    }

    override fun deletePointOption(pointOption: PointOption) {
        commandPointOptionPort.deletePointOption(pointOption)
    }
}
