package team.aliens.dms.domain.notification.service

import java.util.UUID
import org.springframework.stereotype.Component
import team.aliens.dms.domain.notification.exception.DeviceTokenNotFoundException
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.model.Notification
import team.aliens.dms.domain.notification.model.Topic
import team.aliens.dms.domain.notification.spi.DeviceTokenPort
import team.aliens.dms.domain.notification.spi.NotificationPort

@Component
class NotificationServiceImpl(
    private val notificationPort: NotificationPort,
    private val deviceTokenPort: DeviceTokenPort
) : NotificationService {

    override fun saveDeviceToken(deviceToken: DeviceToken) {
        deviceTokenPort.saveDeviceToken(deviceToken)
        notificationPort.subscribeAllTopics(
            deviceToken = deviceToken.deviceToken
        )
    }

    override fun sendNotificationByUserId(userId: UUID, notification: Notification) {
        val deviceToken = this.getDeviceTokenByUserId(userId)
        notificationPort.sendMessage(
            notification = notification,
            deviceToken = deviceToken.deviceToken
        )
    }

    override fun sendNotificationToAll(notification: Notification) {
        notificationPort.sendToAll(notification)
    }

    override fun subscribeTopic(userId: UUID, topic: Topic) {
        val notificationToken = this.getDeviceTokenByUserId(userId)
        notificationPort.subscribeTopic(
            deviceToken = notificationToken.deviceToken,
            topic = topic
        )
    }

    override fun unsubscribeTopic(userId: UUID, topic: Topic) {
        val notificationToken = this.getDeviceTokenByUserId(userId)
        notificationPort.unsubscribeTopic(
            deviceToken = notificationToken.deviceToken,
            topic = topic
        )
    }

    override fun updateSubscribes(userId: UUID, topicSubscribes: List<Pair<Topic, Boolean>>) {
        val notificationToken = this.getDeviceTokenByUserId(userId)
        topicSubscribes.forEach { (topic, isSubscribe) ->
            if (isSubscribe) {
                notificationPort.subscribeTopic(
                    deviceToken = notificationToken.deviceToken,
                    topic = topic
                )
            } else {
                notificationPort.unsubscribeTopic(
                    deviceToken = notificationToken.deviceToken,
                    topic = topic
                )
            }
        }
    }

    private fun getDeviceTokenByUserId(userId: UUID) =
        deviceTokenPort.queryDeviceTokenById(userId) ?: throw DeviceTokenNotFoundException
}
