package team.aliens.dms.thirdparty.notification

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import team.aliens.dms.contract.model.notification.Topic
import team.aliens.dms.domain.notification.model.Notification
import team.aliens.dms.domain.notification.spi.NotificationPort

// dev 서버가 prod 와 같은 Firebase 프로젝트로 토픽 발송을 하면 그 토픽을 구독한 실제 학생 전원에게 간다.
// 그래서 dev 프로파일에서는 FCM 으로 보내지 않고 로그만 남긴다
@Profile("dev")
@Component
class NoOpNotificationAdapter : NotificationPort {

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun sendMessage(token: String, notification: Notification) {
        log.info("[NoOp] skip sendMessage: topic={}, title={}", notification.topic, notification.title)
    }

    override fun sendMessages(tokens: List<String>, notification: Notification) {
        log.info(
            "[NoOp] skip sendMessages: count={}, topic={}, title={}",
            tokens.size, notification.topic, notification.title
        )
    }

    override fun sendByTopic(notification: Notification) {
        log.info("[NoOp] skip sendByTopic: topic={}, title={}", notification.topic, notification.title)
    }

    override fun subscribeTopic(token: String, topic: Topic) {
        log.info("[NoOp] skip subscribeTopic: topic={}", topic)
    }

    override fun subscribeAllTopics(token: String) {
        log.info("[NoOp] skip subscribeAllTopics")
    }

    override fun unsubscribeTopic(token: String, topic: Topic) {
        log.info("[NoOp] skip unsubscribeTopic: topic={}", topic)
    }
}
