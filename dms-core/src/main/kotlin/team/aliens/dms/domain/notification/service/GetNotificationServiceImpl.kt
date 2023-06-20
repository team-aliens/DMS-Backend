package team.aliens.dms.domain.notification.service

import java.util.UUID
import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.notification.exception.DeviceTokenNotFoundException
import team.aliens.dms.domain.notification.model.TopicSubscription
import team.aliens.dms.domain.notification.spi.DeviceTokenPort
import team.aliens.dms.domain.notification.spi.NotificationOfUserPort
import team.aliens.dms.domain.notification.spi.TopicSubscriptionPort

@Service
class GetNotificationServiceImpl(
    private val deviceTokenPort: DeviceTokenPort,
    private val notificationOfUserPort: NotificationOfUserPort,
    private val topicSubscriptionPort: TopicSubscriptionPort
): GetNotificationService {

    override fun getNotificationOfUsersByUserId(userId: UUID) =
        notificationOfUserPort.queryNotificationOfUserByUserId(userId)

    override fun getTopicSubscriptionsByToken(token: String): List<TopicSubscription> {
        val savedToken = getDeviceTokenByToken(token)
        return topicSubscriptionPort.queryTopicSubscriptionsByDeviceTokenId(savedToken.id)
    }

    override fun getDeviceTokenByToken(token: String) =
        deviceTokenPort.queryDeviceTokenByToken(token) ?: throw DeviceTokenNotFoundException
}