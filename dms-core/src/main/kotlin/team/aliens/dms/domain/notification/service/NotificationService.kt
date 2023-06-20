package team.aliens.dms.domain.notification.service

import java.util.UUID
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.model.Notification
import team.aliens.dms.domain.notification.model.NotificationOfUser
import team.aliens.dms.domain.notification.model.Topic
import team.aliens.dms.domain.notification.model.TopicSubscription

interface NotificationService {

    fun saveDeviceToken(deviceToken: DeviceToken)

    fun unsubscribeTopic(token: String, topic: Topic)

    fun subscribeTopic(token: String, topic: Topic)

    fun updateSubscribes(token: String, topicsToSubscribe: List<Pair<Topic, Boolean>>)

    fun sendMessage(
        deviceToken: DeviceToken,
        notification: Notification
    )

    fun sendMessages(deviceTokens: List<DeviceToken>, notification: Notification)

    fun sendMessagesByTopic(
        notification: Notification
    )

    fun getNotificationOfUsersByUserId(userId: UUID): List<NotificationOfUser>

    fun getTopicSubscriptionsByToken(token: String): List<TopicSubscription>
}
