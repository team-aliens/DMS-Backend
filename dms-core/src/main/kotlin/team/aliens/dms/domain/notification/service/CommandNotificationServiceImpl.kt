package team.aliens.dms.domain.notification.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.notification.exception.DeviceTokenNotFoundException
import team.aliens.dms.domain.notification.exception.NotificationOfUserNotFoundException
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.model.NotificationOfUser
import team.aliens.dms.domain.notification.spi.CommandNotificationOfUserPort
import team.aliens.dms.domain.notification.spi.DeviceTokenPort
import team.aliens.dms.domain.notification.spi.NotificationPort
import team.aliens.dms.domain.notification.spi.QueryNotificationOfUserPort
import team.aliens.dms.domain.notification.spi.TopicSubscriptionPort
import java.time.LocalDateTime
import java.util.UUID

@Service
class CommandNotificationServiceImpl(
    private val deviceTokenPort: DeviceTokenPort,
    private val notificationPort: NotificationPort,
    private val queryNotificationOfUserPort: QueryNotificationOfUserPort,
    private val commandNotificationOfUserPort: CommandNotificationOfUserPort,
    private val topicSubscriptionPort: TopicSubscriptionPort
) : CommandNotificationService {

    override fun saveDeviceToken(deviceToken: DeviceToken): DeviceToken {
        return deviceTokenPort.saveDeviceToken(deviceToken).also {
            notificationPort.subscribeAllTopics(
                token = deviceToken.token
            )
        }
    }

    override fun deleteDeviceTokenByUserId(userId: UUID) {
        val deviceToken = deviceTokenPort.queryDeviceTokenByUserId(userId)

        if (deviceToken != null) {
            topicSubscriptionPort.deleteAllByDeviceTokenId(deviceToken.id)
            deviceTokenPort.deleteDeviceTokenByUserId(userId)
        } else {
            throw DeviceTokenNotFoundException
        }
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

    override fun saveNotificationOfUser(notificationOfUser: NotificationOfUser): NotificationOfUser {
        return commandNotificationOfUserPort.saveNotificationOfUser(notificationOfUser)
    }

    override fun saveNotificationsOfUser(notificationOfUsers: List<NotificationOfUser>) {
        commandNotificationOfUserPort.saveNotificationsOfUser(notificationOfUsers)
    }

    override fun deleteOldNotifications() {
        val cutoffDate = LocalDateTime.now().minusDays(60)
        commandNotificationOfUserPort.deleteOldNotificationOfUsers(cutoffDate)
    }
}
