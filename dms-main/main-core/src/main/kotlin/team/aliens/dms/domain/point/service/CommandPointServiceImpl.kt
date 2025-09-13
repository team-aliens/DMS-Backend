package team.aliens.dms.domain.point.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.common.spi.NotificationEventPort
import team.aliens.dms.contract.model.NotificationInfo
import team.aliens.dms.contract.model.Topic
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.point.model.PointOption
import team.aliens.dms.domain.point.spi.CommandPointHistoryPort
import team.aliens.dms.domain.point.spi.CommandPointOptionPort
import team.aliens.dms.domain.user.spi.QueryUserPort
import java.util.UUID

@Service
class CommandPointServiceImpl(
    private val commandPointHistoryPort: CommandPointHistoryPort,
    private val commandPointOptionPort: CommandPointOptionPort,
    private val notificationEventPort: NotificationEventPort,
    private val queryUserPort: QueryUserPort,
) : CommandPointService {

    override fun savePointHistory(pointHistory: PointHistory) =
        commandPointHistoryPort.savePointHistory(pointHistory)

    override fun deletePointHistory(pointHistory: PointHistory) {
        commandPointHistoryPort.deletePointHistory(pointHistory)
    }

    override fun saveAllPointHistories(pointHistories: List<PointHistory>, studentIds: List<UUID>?) {
        val userIds = queryUserPort.queryUsersByStudentIds(studentIds!!)
            .map { it.id }

        val notificationInfo = pointHistories.first().let {
            NotificationInfo(
                schoolId = it.schoolId,
                topic = Topic.POINT,
                linkIdentifier = it.id.toString(),
                title = it.getTitle(),
                content = it.pointName,
                threadId = it.id.toString(),
                isSaveRequired = true
            )
        }

        notificationEventPort.publishNotificationToApplicant(
            userIds, notificationInfo
        )

        commandPointHistoryPort.saveAllPointHistories(pointHistories)
    }

    override fun savePointOption(pointOption: PointOption): PointOption =
        commandPointOptionPort.savePointOption(pointOption)

    override fun deletePointOption(pointOption: PointOption) {
        commandPointOptionPort.deletePointOption(pointOption)
    }
}
