package team.aliens.dms.thirdparty.notification

import com.google.firebase.messaging.ApnsConfig
import com.google.firebase.messaging.Aps
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import org.springframework.stereotype.Component
import team.aliens.dms.domain.notification.model.Notification
import team.aliens.dms.domain.notification.model.Topic
import team.aliens.dms.domain.notification.spi.NotificationPort

@Component
class FCMAdapter : NotificationPort {

    private val firebaseInstance: FirebaseMessaging = FirebaseMessaging.getInstance()

    override fun subscribeTopic(deviceToken: String, topic: Topic) {
        firebaseInstance.subscribeToTopicAsync(listOf(deviceToken), topic.toString())
    }

    override fun subscribeAllTopics(deviceToken: String) {
        Topic.values().map {
            firebaseInstance.subscribeToTopicAsync(listOf(deviceToken), it.toString())
        }
    }

    override fun unsubscribeTopic(deviceToken: String, topic: Topic) {
        firebaseInstance.unsubscribeFromTopicAsync(listOf(deviceToken), topic.toString())
    }

    override fun sendMessage(
        deviceToken: String,
        notification: Notification
    ) {
        val message = this.getMassageBuilderByNotification(notification)
            .setToken(deviceToken)
            .build()
        firebaseInstance.sendAsync(message)
    }

    override fun sendGroupMessage(
        notification: Notification
    ) {
        val message = this.getMassageBuilderByNotification(notification).build()
        firebaseInstance.sendAsync(message)
    }

    private fun getMassageBuilderByNotification(notification: Notification) =
        with(notification) {
            Message
                .builder()
                .setTopic(notification.topic.toString())
                .setNotification(
                    com.google.firebase.messaging.Notification
                        .builder()
                        .setTitle(title)
                        .setBody(content)
                        .build()
                )
                .withApnsConfig(threadId)
        }

    private fun Message.Builder.withApnsConfig(threadId: String) =
        setApnsConfig(
            ApnsConfig
                .builder()
                .setAps(
                    Aps.builder()
                        .setSound("default")
                        .setThreadId(threadId)
                        .build()
                ).build()
        )
}
