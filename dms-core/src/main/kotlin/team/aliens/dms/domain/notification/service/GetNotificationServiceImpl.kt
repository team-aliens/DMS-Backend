package team.aliens.dms.domain.notification.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.common.dto.PageData
import team.aliens.dms.domain.notification.exception.DeviceTokenNotFoundException
import team.aliens.dms.domain.notification.model.OperatingSystem
import team.aliens.dms.domain.notification.model.TopicSubscription
import team.aliens.dms.domain.notification.spi.QueryDeviceTokenPort
import team.aliens.dms.domain.notification.spi.QueryNotificationOfUserPort
import team.aliens.dms.domain.notification.spi.QueryTopicSubscriptionPort
import java.util.UUID

@Service
class GetNotificationServiceImpl(
    private val queryDeviceTokenPort: QueryDeviceTokenPort,
    private val queryNotificationOfUserPort: QueryNotificationOfUserPort,
    private val queryTopicSubscriptionPort: QueryTopicSubscriptionPort
) : GetNotificationService {

    override fun getNotificationOfUsersByUserId(userId: UUID, pageData: PageData) =
        queryNotificationOfUserPort.queryNotificationOfUserByUserId(userId, pageData)

    override fun getTopicSubscriptionsByToken(token: String): List<TopicSubscription> {
        val savedToken = getDeviceTokenByToken(token)
        return queryTopicSubscriptionPort.queryTopicSubscriptionsByDeviceTokenId(savedToken.id)
    }

    override fun getDeviceTokenByOSAndDeviceIdAndUserId(
        operatingSystem: OperatingSystem,
        deviceId: String,
        userId: UUID
    ) = queryDeviceTokenPort.queryDeviceTokenByOSAndDeviceId(
        operatingSystem = operatingSystem,
        deviceId = deviceId
    ).run {
        if (this == null || this.userId != userId) throw DeviceTokenNotFoundException
        return@run this
    }

    override fun getDeviceTokenByToken(token: String) =
        queryDeviceTokenPort.queryDeviceTokenByToken(token) ?: throw DeviceTokenNotFoundException
}
