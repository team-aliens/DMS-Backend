package team.aliens.dms.domain.notification.spi

import team.aliens.dms.domain.notification.model.Notification
import team.aliens.dms.domain.notification.model.Topic

interface NotificationPort {

    fun sendMessage(
        deviceToken: String,
        notification: Notification
    )

    fun sendToAll(
        notification: Notification
    )

    fun subscribeTopic(deviceToken: String, topic: Topic)

    fun subscribeAllTopics(deviceToken: String)

    fun unsubscribeTopic(deviceToken: String, topic: Topic)

    fun sendMessages(deviceTokens: List<String>, notification: Notification)
}
