package team.aliens.dms.thirdparty.notification

import com.google.firebase.messaging.ApnsConfig
import com.google.firebase.messaging.Aps
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.MulticastMessage
import org.springframework.stereotype.Component
import team.aliens.dms.contract.model.Topic
import team.aliens.dms.domain.notification.model.Notification
import team.aliens.dms.domain.notification.spi.NotificationPort

@Component
class FCMAdapter : NotificationPort {

    private val firebaseInstance: FirebaseMessaging
        get() = FirebaseMessaging.getInstance()

    override fun subscribeTopic(token: String, topic: Topic) {
        firebaseInstance.subscribeToTopicAsync(listOf(token), topic.toString())
    }

    override fun subscribeAllTopics(token: String) {
        Topic.values().forEach {
            firebaseInstance.subscribeToTopicAsync(listOf(token), it.toString())
        }
    }

    override fun unsubscribeTopic(token: String, topic: Topic) {
        firebaseInstance.unsubscribeFromTopicAsync(listOf(token), topic.toString())
    }

    override fun sendMessage(
        token: String,
        notification: Notification
    ) {
        val message = this.getMassageBuilderByNotification(notification)
            .setToken(token)
            .build()
        firebaseInstance.sendAsync(message)
    }

    override fun sendByTopic(
        notification: Notification
    ) {
        val message = this.getMassageBuilderByNotification(notification).build()
        firebaseInstance.sendAsync(message)
    }

    private fun getMassageBuilderByNotification(notification: Notification) =
        with(notification) {
            Message
                .builder()
                .setTopic(topic.toString())
                .setNotification(
                    com.google.firebase.messaging.Notification
                        .builder()
                        .setTitle(title)
                        .setBody(content)
                        .build()
                )
                .setApnsConfig(getApnsConfig(threadId))
        }

    override fun sendMessages(
        tokens: List<String>,
        notification: Notification
    ) {
        val message = this.getMulticastMassageBuilderByNotification(notification)
            .addAllTokens(tokens)
            .build()
        firebaseInstance.sendMulticastAsync(message)
    }

    private fun getMulticastMassageBuilderByNotification(notification: Notification) =
        with(notification) {
            MulticastMessage
                .builder()
                .setNotification(
                    com.google.firebase.messaging.Notification
                        .builder()
                        .setTitle(title)
                        .setBody(content)
                        .build()
                )
                .setApnsConfig(getApnsConfig(threadId))
        }

    private fun getApnsConfig(threadId: String) =
        ApnsConfig
            .builder()
            .setAps(
                Aps.builder()
                    .setSound("default")
                    .setThreadId(threadId)
                    .build()
            ).build()
}
