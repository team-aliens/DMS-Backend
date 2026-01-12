package team.aliens.dms.domain.point.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.common.spi.NotificationEventPort
import team.aliens.dms.contract.model.notification.NotificationInfo
import team.aliens.dms.contract.model.notification.Topic
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

    override fun saveAllPointHistories(
        pointHistories: List<PointHistory>,
        studentIds: List<UUID>?
    ): List<PointHistory> {

        val saved = commandPointHistoryPort.saveAllPointHistories(pointHistories)

        val first = saved.first()

        val userIds = queryUserPort.queryUsersByStudentIds(studentIds!!)
            .map { it.id }

        val notificationInfo = NotificationInfo(
            schoolId = first.schoolId,
            topic = Topic.POINT,
            pointDetailTopic = first.getPointDetailTopic(),
            linkIdentifier = first.id.toString(),
            title = first.getTitle(),
            content = first.pointName,
            threadId = first.id.toString(),
            isSaveRequired = true
        )

        notificationEventPort.publishNotificationToApplicant(userIds, notificationInfo)

        return saved
    }

    override fun savePointOption(pointOption: PointOption): PointOption =
        commandPointOptionPort.savePointOption(pointOption)

    override fun deletePointOption(pointOption: PointOption) {
        commandPointOptionPort.deletePointOption(pointOption)
    }
}
