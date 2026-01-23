package team.aliens.dms.domain.notification.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.notification.exception.DeviceTokenNotFoundException
import team.aliens.dms.domain.notification.exception.NotificationOfUserNotFoundException
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.spi.QueryDeviceTokenPort
import team.aliens.dms.domain.notification.spi.QueryNotificationOfUserPort
import team.aliens.dms.domain.notification.spi.QueryTopicSubscriptionPort
import java.util.UUID

@Service
class GetNotificationServiceImpl(
    private val deviceTokenPort: QueryDeviceTokenPort,
    private val notificationOfUserPort: QueryNotificationOfUserPort,
    private val topicSubscriptionPort: QueryTopicSubscriptionPort
) : GetNotificationService {

    override fun getNotificationOfUsersByUserId(userId: UUID) =
        notificationOfUserPort.queryNotificationOfUserByUserId(userId)

    override fun getNotificationOfUserById(notificationOfUserId: UUID) =
        notificationOfUserPort.queryNotificationOfUserById(notificationOfUserId)
            ?: throw NotificationOfUserNotFoundException

    override fun getTopicSubscriptionsByToken(token: String) =
        topicSubscriptionPort.queryTopicSubscriptionsByDeviceTokenId(getDeviceTokenByToken(token).id)

    override fun getDeviceTokenByToken(token: String) =
        deviceTokenPort.queryDeviceTokenByToken(token) ?: throw DeviceTokenNotFoundException

    override fun getDeviceTokenByUserId(userId: UUID): DeviceToken {
        return deviceTokenPort.queryDeviceTokenByUserId(userId) ?: throw DeviceTokenNotFoundException
    }

    override fun getDiviceTokensByUserIds(userIds: List<UUID>): List<DeviceToken> {
        return deviceTokenPort.queryDeviceTokensByUserIds(userIds)
    }
}
