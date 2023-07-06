package team.aliens.dms.domain.notification.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.notification.exception.NotificationOfUserNotFoundException
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.spi.CommandDeviceTokenPort
import team.aliens.dms.domain.notification.spi.CommandNotificationOfUserPort
import team.aliens.dms.domain.notification.spi.NotificationPort
import team.aliens.dms.domain.notification.spi.QueryDeviceTokenPort
import team.aliens.dms.domain.notification.spi.QueryNotificationOfUserPort
import team.aliens.dms.domain.notification.spi.QueryTopicSubscriptionPort
import java.util.UUID

@Service
class CommandNotificationServiceImpl(
    private val commandDeviceTokenPort: CommandDeviceTokenPort,
    private val queryDeviceTokenPort: QueryDeviceTokenPort,
    private val notificationPort: NotificationPort,
    private val queryTopicSubscriptionPort: QueryTopicSubscriptionPort,
    private val queryNotificationOfUserPort: QueryNotificationOfUserPort,
    private val commandNotificationOfUserPort: CommandNotificationOfUserPort,
    private val notificationServiceImpl: NotificationServiceImpl
) : CommandNotificationService {

    override fun createOrUpdateDeviceToken(deviceToken: DeviceToken) {
        val savedDeviceToken = queryDeviceTokenPort.queryDeviceTokenByOSAndDeviceId(deviceToken.operatingSystem, deviceToken.deviceId)

        if (savedDeviceToken == null) {
            commandDeviceTokenPort.saveDeviceToken(deviceToken)
            notificationPort.subscribeAllTopics(
                token = deviceToken.token
            )
        } else {
            commandDeviceTokenPort.saveDeviceToken(
                deviceToken.copy(id = savedDeviceToken.id)
            )
            notificationServiceImpl.updateSubscribes(
                token = deviceToken.token,
                topicsToSubscribe = queryTopicSubscriptionPort.queryTopicSubscriptionsByDeviceTokenId(deviceToken.id)
                    .map { it.topic to it.isSubscribed }
            )
        }
    }

    override fun deleteDeviceTokenById(deviceTokenId: UUID) {
        commandDeviceTokenPort.deleteDeviceTokenById(deviceTokenId)
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
