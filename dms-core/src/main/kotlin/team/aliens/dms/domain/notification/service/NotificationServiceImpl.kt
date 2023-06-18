package team.aliens.dms.domain.notification.service

import org.springframework.stereotype.Component
import team.aliens.dms.domain.notification.exception.DeviceTokenNotFoundException
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.model.Notification
import team.aliens.dms.domain.notification.model.Topic
import team.aliens.dms.domain.notification.model.TopicSubscribe
import team.aliens.dms.domain.notification.spi.DeviceTokenPort
import team.aliens.dms.domain.notification.spi.NotificationPort
import team.aliens.dms.domain.notification.spi.TopicSubscribePort

@Component
class NotificationServiceImpl(
    private val notificationPort: NotificationPort,
    private val topicSubscribePort: TopicSubscribePort,
    private val deviceTokenPort: DeviceTokenPort
) : NotificationService {

    override fun saveDeviceToken(deviceToken: DeviceToken) {
        deviceTokenPort.saveDeviceToken(deviceToken)
        notificationPort.subscribeAllTopics(
            deviceToken = deviceToken.deviceToken
        )
    }

    override fun subscribeTopic(deviceToken: String, topic: Topic) {
        val savedDeviceToken = this.getDeviceTokenByDeviceToken(deviceToken)
        topicSubscribePort.saveTopicSubscribe(
            TopicSubscribe(
                deviceTokenId = savedDeviceToken.id,
                topic = topic,
                isSubscribed = true
            )
        )
        notificationPort.subscribeTopic(
            deviceToken = this.getDeviceTokenByDeviceToken(deviceToken).deviceToken,
            topic = topic
        )
    }

    override fun unsubscribeTopic(deviceToken: String, topic: Topic) {
        val savedDeviceToken = this.getDeviceTokenByDeviceToken(deviceToken)
        topicSubscribePort.saveTopicSubscribe(
            TopicSubscribe(
                deviceTokenId = savedDeviceToken.id,
                topic = topic,
                isSubscribed = false
            )
        )
        notificationPort.unsubscribeTopic(
            deviceToken = deviceToken,
            topic = topic
        )
    }

    override fun updateSubscribes(deviceToken: String, topicsToSubscribe: List<Pair<Topic, Boolean>>) {

        val savedDeviceToken = this.getDeviceTokenByDeviceToken(deviceToken)

        topicsToSubscribe.forEach { (topic, isSubscribe) ->
            if (isSubscribe) {
                notificationPort.subscribeTopic(
                    deviceToken = deviceToken,
                    topic = topic
                )
            } else {
                notificationPort.unsubscribeTopic(
                    deviceToken = deviceToken,
                    topic = topic
                )
            }
        }

        topicSubscribePort.saveAllTopicSubscribes(
            topicsToSubscribe.map {
                TopicSubscribe(
                    deviceTokenId = savedDeviceToken.id,
                    topic = it.first,
                    isSubscribed = it.second
                )
            }
        )
    }

    private fun getDeviceTokenByDeviceToken(deviceToken: String) =
        deviceTokenPort.queryDeviceTokenByDeviceToken(deviceToken) ?: throw DeviceTokenNotFoundException

    override fun sendMessage(deviceToken: String, notification: Notification) {
        notificationPort.sendMessage(
            deviceToken = deviceToken,
            notification = notification
        )
    }

    override fun sendMessages(deviceTokens: List<String>, notification: Notification) {
        notificationPort.sendMessages(
            deviceTokens = deviceTokens,
            notification = notification
        )
    }

    override fun sendMessagesByTopic(notification: Notification) {
        notificationPort.sendByTopic(
            notification = notification
        )
    }

    override fun getTopicSubscribesByDeviceToken(deviceToken: String): List<TopicSubscribe> {
        val savedToken = getDeviceTokenByDeviceToken(deviceToken)
        return topicSubscribePort.queryTopicSubscribesByDeviceTokenId(savedToken.id)
    }
}
