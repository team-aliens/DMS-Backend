package team.aliens.dms.domain.notification.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.notification.exception.NotificationOfUserNotFoundException
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.spi.CommandDeviceTokenPort
import team.aliens.dms.domain.notification.spi.CommandNotificationOfUserPort
import team.aliens.dms.domain.notification.spi.NotificationPort
import team.aliens.dms.domain.notification.spi.QueryNotificationOfUserPort
import java.util.UUID

@Service
class CommandNotificationServiceImpl(
    private val deviceTokenPort: CommandDeviceTokenPort,
    private val notificationPort: NotificationPort,
    private val queryNotificationOfUserPort: QueryNotificationOfUserPort,
    private val commandNotificationOfUserPort: CommandNotificationOfUserPort
) : CommandNotificationService {

    override fun saveDeviceToken(deviceToken: DeviceToken) {
        deviceTokenPort.saveDeviceToken(deviceToken)
        notificationPort.subscribeAllTopics(
            token = deviceToken.token
        )
    }

    override fun deleteNotificationOfUserByUserIdAndId(userId: UUID, notificationOfUserId: UUID) {
        queryNotificationOfUserPort.queryNotificationOfUserById(notificationOfUserId).also {
            if (it == null || it.userId != userId) {
                throw NotificationOfUserNotFoundException
            }
        }
        commandNotificationOfUserPort.deleteNotificationOfUserById(notificationOfUserId)
    }

    override fun deleteNotificationOfUserByUserId(userId: UUID) {
        commandNotificationOfUserPort.deleteNotificationOfUserByUserId(userId)
    }
}
