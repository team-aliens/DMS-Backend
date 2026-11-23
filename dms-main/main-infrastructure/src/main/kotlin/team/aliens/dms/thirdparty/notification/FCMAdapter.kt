package team.aliens.dms.thirdparty.notification

import com.google.firebase.ErrorCode
import com.google.firebase.messaging.ApnsConfig
import com.google.firebase.messaging.Aps
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.MessagingErrorCode
import com.google.firebase.messaging.MulticastMessage
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import team.aliens.dms.contract.model.notification.Topic
import team.aliens.dms.domain.notification.exception.NotificationSendFailedException
import team.aliens.dms.domain.notification.model.Notification
import team.aliens.dms.domain.notification.spi.NotificationPort

@Profile("!dev")
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
    ): List<String> {
        val batchResponse = try {
            getMulticastMessageBuilderByNotification(notification)
                .addAllTokens(tokens)
                .build()
                .let { firebaseInstance.sendEachForMulticast(it) }
        } catch (e: FirebaseMessagingException) {
            log.error("Failed to send multicast notification", e)
            throw NotificationSendFailedException
        }

        if (batchResponse.failureCount == 0) {
            return emptyList()
        }

        val retryableTokens = mutableListOf<String>()
        batchResponse.responses.forEachIndexed { index, response ->
            if (!response.isSuccessful) {
                log.error(
                    "Failed to send notification to token: {}, error: {}",
                    tokens[index],
                    response.exception?.message
                )
                if (isRetryable(response.exception)) {
                    retryableTokens.add(tokens[index])
                }
            }
        }
        log.warn(
            "Failed to send notifications to {} tokens. Success: {}, Retryable: {}",
            batchResponse.failureCount,
            batchResponse.successCount,
            retryableTokens.size
        )
        return retryableTokens
    }

    // 재시도하지 않으면 outbox 행이 지워지므로, 다시 보내도 실패가 확실한 것만 제외한다.
    // 401·403(서버 키·APNs 인증·SENDER_ID_MISMATCH)은 서버 설정을 고치면 풀리므로 재시도하고, 소진되면 FAILED 로 남긴다
    private fun isRetryable(exception: FirebaseMessagingException?): Boolean {
        if (exception == null) return true
        exception.messagingErrorCode?.let { return it !in PERMANENT_MESSAGING_ERROR_CODES }
        return exception.errorCode != ErrorCode.INVALID_ARGUMENT
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

    private fun getApnsConfig(threadId: String) =
        ApnsConfig
            .builder()
            .setAps(
                Aps.builder()
                    .setSound("default")
                    .setThreadId(threadId)
                    .build()
            ).build()

    companion object {
        // 앱 삭제·만료된 토큰, 잘못된 토큰·메시지 형식
        private val PERMANENT_MESSAGING_ERROR_CODES = setOf(
            MessagingErrorCode.UNREGISTERED,
            MessagingErrorCode.INVALID_ARGUMENT
        )
    }
}
