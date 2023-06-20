package team.aliens.dms.domain.notification.spi

import team.aliens.dms.domain.notification.model.Notification
import team.aliens.dms.domain.notification.model.Topic

interface NotificationPort {

    fun sendMessage(
        token: String,
        notification: Notification
    )

    fun sendMessages(tokens: List<String>, notification: Notification)

    fun sendByTopic(
        notification: Notification
    )

    fun subscribeTopic(token: String, topic: Topic)

    fun subscribeAllTopics(token: String)

    fun unsubscribeTopic(token: String, topic: Topic)
}
