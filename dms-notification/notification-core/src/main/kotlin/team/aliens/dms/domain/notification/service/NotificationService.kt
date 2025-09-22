package team.aliens.dms.domain.notification.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.contract.model.Topic
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.model.Notification

@Service
interface NotificationService :
    GetNotificationService,
    CheckNotificationService,
    CommandNotificationService {

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

    fun toggleSubscription(token: String, topic: Topic)
}
