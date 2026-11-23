package team.aliens.dms.domain.notification.service

import team.aliens.dms.contract.model.notification.Topic
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.model.Notification
import team.aliens.dms.domain.notification.model.NotificationOfUser
import team.aliens.dms.domain.notification.model.NotificationOutbox
import java.util.UUID

interface CommandNotificationService {

    fun saveDeviceToken(deviceToken: DeviceToken): DeviceToken

    fun deleteDeviceTokenByUserId(userId: UUID)

    fun deleteNotificationOfUserByUserIdAndId(userId: UUID, notificationOfUserId: UUID)

    fun deleteNotificationOfUserByUserId(userId: UUID)

    fun saveNotificationOfUser(notificationOfUser: NotificationOfUser): NotificationOfUser

    fun saveNotificationsOfUser(notificationOfUsers: List<NotificationOfUser>)

    fun deleteOldNotifications()

    fun subscribeTopic(token: String, topic: Topic)

    fun unsubscribeTopic(token: String, topic: Topic)

    fun updateSubscribes(token: String, topicsToSubscribe: List<Pair<Topic, Boolean>>)

    fun toggleSubscription(token: String, topic: Topic)

    fun sendMessagesByTopic(
        notification: Notification
    )

    // 처리할 행이 없거나(이미 처리됨·다른 곳에서 처리 중) 전부 발송되면 null, 다시 보낼 게 남으면 남은 outbox 를 돌려준다
    fun processNotificationOutbox(outboxId: UUID): NotificationOutbox?

    // 발송 시도가 예외로 롤백됐을 때 재시도 횟수를 올린다. 다음 스케줄러 실행이 다시 보낸다
    fun deferNotificationOutbox(outboxId: UUID): NotificationOutbox?
}
