package team.aliens.dms.domain.notification.service

import java.util.UUID
import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.notification.exception.NotificationOfUserNotFoundException
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.spi.DeviceTokenPort
import team.aliens.dms.domain.notification.spi.NotificationOfUserPort
import team.aliens.dms.domain.notification.spi.NotificationPort

@Service
class CommandNotificationServiceImpl(
    private val deviceTokenPort: DeviceTokenPort,
    private val notificationPort: NotificationPort,
    private val notificationOfUserPort: NotificationOfUserPort
): CommandNotificationService {

    override fun saveDeviceToken(deviceToken: DeviceToken) {
        deviceTokenPort.saveDeviceToken(deviceToken)
        notificationPort.subscribeAllTopics(
            token = deviceToken.token
        )
    }

    override fun deleteNotificationOfUserByUserIdAndId(userId: UUID, notificationOfUserId: UUID) {
        notificationOfUserPort.queryNotificationOfUserById(notificationOfUserId).also {
            if (it == null || it.userId != userId) {
                throw NotificationOfUserNotFoundException
            }
        }
        notificationOfUserPort.deleteNotificationOfUserById(notificationOfUserId)
    }

    override fun deleteNotificationOfUserByUserId(userId: UUID) {
        notificationOfUserPort.deleteNotificationOfUserByUserId(userId)
    }
}