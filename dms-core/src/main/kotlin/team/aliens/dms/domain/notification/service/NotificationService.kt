package team.aliens.dms.domain.notification.service

import team.aliens.dms.domain.notification.dto.TopicSubscribeResponse
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.model.Notification
import team.aliens.dms.domain.notification.model.Topic
import java.util.UUID

interface NotificationService {

    fun saveDeviceToken(deviceToken: DeviceToken)

    fun unsubscribeTopic(deviceToken: String, topic: Topic)

    fun subscribeTopic(deviceToken: String, topic: Topic)

    fun updateSubscribes(deviceToken: String, topicsToSubscribe: List<Pair<Topic, Boolean>>)

    fun sendMessage(
        deviceToken: String,
        notification: Notification
    )

    fun sendMessages(deviceTokens: List<String>, notification: Notification)

    fun sendMessagesByTopic(
        notification: Notification
    )

    fun getTopicSubscribesByUserId(userId: UUID): List<TopicSubscribeResponse>
}
