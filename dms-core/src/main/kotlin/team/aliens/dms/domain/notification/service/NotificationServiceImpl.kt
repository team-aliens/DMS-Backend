package team.aliens.dms.domain.notification.service

import java.util.UUID
import org.springframework.stereotype.Component
import team.aliens.dms.common.spi.EventPort
import team.aliens.dms.domain.notification.exception.DeviceTokenNotFoundException
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.model.Notification
import team.aliens.dms.domain.notification.model.Topic
import team.aliens.dms.domain.notification.spi.DeviceTokenPort
import team.aliens.dms.domain.notification.spi.NotificationPort

@Component
class NotificationServiceImpl(
    private val notificationPort: NotificationPort,
    private val deviceTokenPort: DeviceTokenPort,
    private val eventPort: EventPort
) : NotificationService {

    override fun saveDeviceToken(deviceToken: DeviceToken) {
        deviceTokenPort.saveDeviceToken(deviceToken)
        notificationPort.subscribeAllTopics(
            deviceToken = deviceToken.deviceToken
        )
    }

    override fun subscribeTopic(userId: UUID, topic: Topic) {
        val deviceToken = this.getDeviceTokenByUserId(userId)
        notificationPort.subscribeTopic(
            deviceToken = deviceToken.deviceToken,
            topic = topic
        )
    }

    override fun unsubscribeTopic(userId: UUID, topic: Topic) {
        val deviceToken = this.getDeviceTokenByUserId(userId)
        notificationPort.unsubscribeTopic(
            deviceToken = deviceToken.deviceToken,
            topic = topic
        )
    }

    override fun updateSubscribes(userId: UUID, topicsToSubscribe: List<Pair<Topic, Boolean>>) {

        val deviceToken = this.getDeviceTokenByUserId(userId)
        topicsToSubscribe.forEach { (topic, isSubscribe) ->
            if (isSubscribe) {
                notificationPort.subscribeTopic(
                    deviceToken = deviceToken.deviceToken,
                    topic = topic
                )
            } else {
                notificationPort.unsubscribeTopic(
                    deviceToken = deviceToken.deviceToken,
                    topic = topic
                )
            }
        }
    }

    private fun getDeviceTokenByUserId(userId: UUID) =
        deviceTokenPort.queryDeviceTokenById(userId) ?: throw DeviceTokenNotFoundException

    override fun publishNotification(
        deviceToken: String,
        notification: Notification
    ) {
        eventPort.publishNotification(
            deviceToken = deviceToken,
            notification = notification
        )
    }

    override fun publishNotificationToAll(
        notification: Notification
    ) {
        eventPort.publishNotificationToAll(
            notification = notification
        )
    }
}
