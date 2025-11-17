package team.aliens.dms.thirdparty.notification

import com.google.firebase.messaging.ApnsConfig
import com.google.firebase.messaging.Aps
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.MulticastMessage
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import team.aliens.dms.contract.model.notification.Topic
import team.aliens.dms.domain.notification.exception.NotificationSendFailedException
import team.aliens.dms.domain.notification.model.Notification
import team.aliens.dms.domain.notification.spi.NotificationPort

@Component
class FCMAdapter : NotificationPort {

    private val log = LoggerFactory.getLogger(this::class.java)

    private val firebaseInstance: FirebaseMessaging
        get() = FirebaseMessaging.getInstance()

    override fun subscribeTopic(token: String, topic: Topic) {
        firebaseInstance.subscribeToTopic(listOf(token), topic.toString())
    }

    override fun subscribeAllTopics(token: String) {
        Topic.values().forEach {
            firebaseInstance.subscribeToTopic(listOf(token), it.toString())
        }
    }

    override fun unsubscribeTopic(token: String, topic: Topic) {
        firebaseInstance.unsubscribeFromTopic(listOf(token), topic.toString())
    }

    override fun sendMessage(
        token: String,
        notification: Notification
    ) {
        try {
            getMessageBuilderByNotification(notification)
                .setToken(token)
                .build()
                .also { firebaseInstance.send(it) }
        } catch (e: FirebaseMessagingException) {
            log.error("Failed to send notification to token: {}", token, e)
            throw NotificationSendFailedException
        }
    }

    override fun sendByTopic(
        notification: Notification
    ) {
        try {
            getMessageBuilderByNotification(notification)
                .build()
                .also { firebaseInstance.send(it) }
        } catch (e: FirebaseMessagingException) {
            log.error("Failed to send notification to topic: {}", notification.topic, e)
            throw NotificationSendFailedException
        }
    }

    private fun getMessageBuilderByNotification(notification: Notification) =
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
        try {
            getMulticastMessageBuilderByNotification(notification)
                .addAllTokens(tokens)
                .build()
                .let { firebaseInstance.sendEachForMulticast(it) }
                .also { batchResponse ->
                    if (batchResponse.failureCount > 0) {
                        batchResponse.responses.forEachIndexed { index, response ->
                            if (!response.isSuccessful) {
                                log.error(
                                    "Failed to send notification to token: {}, error: {}",
                                    tokens[index],
                                    response.exception?.message
                                )
                            }
                        }
                        log.warn(
                            "Failed to send notifications to {} tokens. Success: {}",
                            batchResponse.failureCount,
                            batchResponse.successCount
                        )
                    }
                }
        } catch (e: FirebaseMessagingException) {
            log.error("Failed to send multicast notification", e)
            throw NotificationSendFailedException
        }
    }

    private fun getMulticastMessageBuilderByNotification(notification: Notification) =
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
