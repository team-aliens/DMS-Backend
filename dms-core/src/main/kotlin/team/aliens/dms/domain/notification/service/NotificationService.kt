package team.aliens.dms.domain.notification.service

import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.model.Notification
import team.aliens.dms.domain.notification.model.Topic
import team.aliens.dms.domain.notification.model.TopicSubscribe

interface NotificationService {

    fun saveDeviceToken(deviceToken: DeviceToken)

    fun unsubscribeTopic(token: String, topic: Topic)

    fun subscribeTopic(token: String, topic: Topic)

    fun updateSubscribes(token: String, topicsToSubscribe: List<Pair<Topic, Boolean>>)

    fun sendMessage(
        token: String,
        notification: Notification
    )

    fun sendMessages(deviceTokens: List<String>, notification: Notification)

    fun sendMessagesByTopic(
        notification: Notification
    )

    fun getTopicSubscribesByToken(token: String): List<TopicSubscribe>
}
