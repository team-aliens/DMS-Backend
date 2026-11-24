package team.aliens.dms.domain.notification.spi

import team.aliens.dms.contract.model.notification.Topic
import team.aliens.dms.domain.notification.model.Notification

interface NotificationPort {

    // 일시적 오류(FCM 과부하·내부 오류 등)로 실패해 다시 보내야하는 토큰을 돌려준다
    fun sendMessages(tokens: List<String>, notification: Notification): List<String>

    fun sendByTopic(
        notification: Notification
    )

    fun subscribeTopic(token: String, topic: Topic)

    fun subscribeAllTopics(token: String)

    fun unsubscribeTopic(token: String, topic: Topic)
}
