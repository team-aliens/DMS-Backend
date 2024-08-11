package team.aliens.dms.domain.point.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.common.spi.NotificationEventPort
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.notification.model.Notification
import team.aliens.dms.domain.notification.spi.QueryDeviceTokenPort
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.point.model.PointOption
import team.aliens.dms.domain.point.spi.CommandPointHistoryPort
import team.aliens.dms.domain.point.spi.CommandPointOptionPort
import team.aliens.dms.domain.student.spi.QueryStudentPort

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

    override fun saveAllPointHistories(pointHistories: List<PointHistory>) {
        val schoolId = securityPort.getCurrentUserSchoolId()

        pointHistories.forEach { pointHistory ->
            val (grade, classRoom, number) = parseGcn(pointHistory.studentGcn)

            val student = queryStudentPort.queryStudentBySchoolIdAndGcn(schoolId, grade, classRoom, number)

            val deviceToken = student?.let {
                deviceTokenPort.queryDeviceTokenByUserId(it.userId!!)
            }

            if (deviceToken != null) {
                notificationEventPort.publishNotificationToApplicant(
                    listOf(deviceToken),
                    Notification.PointNotification(pointHistory)
                )
            }
        }

        commandPointHistoryPort.saveAllPointHistories(pointHistories)
    }

    private fun parseGcn(studentGcn: String): Triple<Int, Int, Int> {
        val parts = studentGcn.split("-").map { it.toInt() }
        return Triple(parts[0], parts[1], parts[2])
    }

    override fun savePointOption(pointOption: PointOption): PointOption =
        commandPointOptionPort.savePointOption(pointOption)

    override fun deletePointOption(pointOption: PointOption) {
        commandPointOptionPort.deletePointOption(pointOption)
    }
}
