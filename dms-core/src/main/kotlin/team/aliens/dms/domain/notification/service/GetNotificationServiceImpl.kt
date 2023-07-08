package team.aliens.dms.domain.notification.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.notification.exception.DeviceTokenNotFoundException
import team.aliens.dms.domain.notification.model.TopicSubscription
import team.aliens.dms.domain.notification.spi.QueryDeviceTokenPort
import team.aliens.dms.domain.notification.spi.QueryNotificationOfUserPort
import team.aliens.dms.domain.notification.spi.QueryTopicSubscriptionPort
import java.util.UUID
import team.aliens.dms.common.dto.PageData

@Service
class GetNotificationServiceImpl(
    private val deviceTokenPort: QueryDeviceTokenPort,
    private val notificationOfUserPort: QueryNotificationOfUserPort,
    private val topicSubscriptionPort: QueryTopicSubscriptionPort
) : GetNotificationService {

    override fun getNotificationOfUsersByUserId(userId: UUID, pageData: PageData) =
        notificationOfUserPort.queryNotificationOfUserByUserId(userId, pageData)

    override fun getTopicSubscriptionsByToken(token: String): List<TopicSubscription> {
        val savedToken = getDeviceTokenByToken(token)
        return topicSubscriptionPort.queryTopicSubscriptionsByDeviceTokenId(savedToken.id)
    }

    override fun getDeviceTokenByToken(token: String) =
        deviceTokenPort.queryDeviceTokenByToken(token) ?: throw DeviceTokenNotFoundException
}
