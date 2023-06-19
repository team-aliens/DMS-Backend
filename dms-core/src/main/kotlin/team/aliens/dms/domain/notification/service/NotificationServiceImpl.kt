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
            token = deviceToken.token
        )
    }

    override fun subscribeTopic(token: String, topic: Topic) {
        val deviceToken = this.getDeviceTokenByToken(token)
        topicSubscribePort.saveTopicSubscribe(
            TopicSubscribe.subscribe(
                deviceTokenId = deviceToken.id,
                topic = topic,
            )
        )
        notificationPort.subscribeTopic(
            token = this.getDeviceTokenByToken(token).token,
            topic = topic
        )
    }

    override fun unsubscribeTopic(token: String, topic: Topic) {
        val deviceToken = this.getDeviceTokenByToken(token)
        topicSubscribePort.saveTopicSubscribe(
            TopicSubscribe.unsubscribe(
                deviceTokenId = deviceToken.id,
                topic = topic,
            )
        )
        notificationPort.unsubscribeTopic(
            token = token,
            topic = topic
        )
    }

    override fun updateSubscribes(token: String, topicsToSubscribe: List<Pair<Topic, Boolean>>) {
        val deviceToken = this.getDeviceTokenByToken(token)
        val topicSubscribes = topicsToSubscribe.map { (topic, isSubscribe) ->
            this.subscribeOrUnsubscribeTopic(isSubscribe, token, topic)
            TopicSubscribe(
                deviceTokenId = deviceToken.id,
                topic = topic,
                isSubscribed = isSubscribe
            )
        }
        topicSubscribePort.saveAllTopicSubscribes(topicSubscribes)
    }

    private fun subscribeOrUnsubscribeTopic(
        isSubscribe: Boolean,
        token: String,
        topic: Topic
    ) {
        if (isSubscribe) {
            notificationPort.subscribeTopic(
                token = token,
                topic = topic
            )
        } else {
            notificationPort.unsubscribeTopic(
                token = token,
                topic = topic
            )
        }
    }

    private fun getDeviceTokenByToken(token: String) =
        deviceTokenPort.queryDeviceTokenByToken(token) ?: throw DeviceTokenNotFoundException

    override fun sendMessage(token: String, notification: Notification) {
        notificationPort.sendMessage(
            token = token,
            notification = notification
        )
    }

    override fun sendMessages(deviceTokens: List<String>, notification: Notification) {
        notificationPort.sendMessages(
            tokens = deviceTokens,
            notification = notification
        )
    }

    override fun sendMessagesByTopic(notification: Notification) {
        notificationPort.sendByTopic(
            notification = notification
        )
    }

    override fun getTopicSubscribesByToken(token: String): List<TopicSubscribe> {
        val savedToken = getDeviceTokenByToken(token)
        return topicSubscribePort.queryTopicSubscribesByDeviceTokenId(savedToken.id)
    }
}
