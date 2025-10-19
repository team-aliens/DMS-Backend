package team.aliens.dms.domain.notification.service

import org.springframework.transaction.annotation.Transactional
import team.aliens.dms.common.annotation.Service
import team.aliens.dms.contract.model.notification.Topic
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.model.Notification
import team.aliens.dms.domain.notification.model.TopicSubscription
import team.aliens.dms.domain.notification.spi.CommandNotificationOfUserPort
import team.aliens.dms.domain.notification.spi.CommandTopicSubscriptionPort
import team.aliens.dms.domain.notification.spi.NotificationPort
import team.aliens.dms.domain.notification.spi.QueryDeviceTokenPort
import team.aliens.dms.domain.notification.spi.QueryTopicSubscriptionPort

@Service
@Transactional
class NotificationServiceImpl(
    private val notificationPort: NotificationPort,
    private val queryDeviceTokenPort: QueryDeviceTokenPort,
    private val commandTopicSubscriptionPort: CommandTopicSubscriptionPort,
    private val notificationOfUserPort: CommandNotificationOfUserPort,
    private val queryTopicSubscriptionPort: QueryTopicSubscriptionPort,
    getNotificationService: GetNotificationService,
    checkNotificationService: CheckNotificationService,
    commandNotificationService: CommandNotificationService
) : NotificationService,
    GetNotificationService by getNotificationService,
    CheckNotificationService by checkNotificationService,
    CommandNotificationService by commandNotificationService {

    override fun subscribeTopic(token: String, topic: Topic) {
        val deviceToken = this.getDeviceTokenByToken(token)
        commandTopicSubscriptionPort.saveTopicSubscription(
            TopicSubscription.subscribe(
                deviceTokenId = deviceToken.id,
                topic = topic,
            )
        )
        notificationPort.subscribeTopic(
            token = deviceToken.token,
            topic = topic
        )
    }

    override fun unsubscribeTopic(token: String, topic: Topic) {
        val deviceToken = this.getDeviceTokenByToken(token)
        commandTopicSubscriptionPort.saveTopicSubscription(
            TopicSubscription.unsubscribe(
                deviceTokenId = deviceToken.id,
                topic = topic,
            )
        )
        notificationPort.unsubscribeTopic(
            token = deviceToken.token,
            topic = topic
        )
    }

    override fun updateSubscribes(token: String, topicsToSubscribe: List<Pair<Topic, Boolean>>) {
        val deviceToken = this.getDeviceTokenByToken(token)
        val topicSubscriptions = topicsToSubscribe.map { (topic, isSubscribe) ->
            this.subscribeOrUnsubscribeTopic(isSubscribe, token, topic)
            TopicSubscription(
                deviceTokenId = deviceToken.id,
                topic = topic,
                isSubscribed = isSubscribe
            )
        }
        commandTopicSubscriptionPort.saveAllTopicSubscriptions(topicSubscriptions)
    }

    private fun subscribeOrUnsubscribeTopic(
        isSubscribe: Boolean,
        token: String,
        topic: Topic
    ) {
        if (isSubscribe) {
            notificationPort.subscribeTopic(
                token = token,
                topic = topic
            )
        } else {
            notificationPort.unsubscribeTopic(
                token = token,
                topic = topic
            )
        }
    }

    override fun sendMessage(deviceToken: DeviceToken, notification: Notification) {
        notification.runIfSaveRequired {
            notificationOfUserPort.saveNotificationOfUser(
                notification.toNotificationOfUser(deviceToken.userId)
            )
        }
        notificationPort.sendMessage(
            token = deviceToken.token,
            notification = notification
        )
    }

    override fun sendMessages(deviceTokens: List<DeviceToken>, notification: Notification) {
        notification.runIfSaveRequired {
            notificationOfUserPort.saveNotificationsOfUser(
                deviceTokens.map { notification.toNotificationOfUser(it.userId) }
            )
        }
        notificationPort.sendMessages(
            tokens = deviceTokens.map { it.token },
            notification = notification
        )
    }

    override fun sendMessagesByTopic(notification: Notification) {
        notification.runIfSaveRequired {
            val userIds = queryDeviceTokenPort.queryDeviceTokensBySubscriptionTopicAndSchoolId(
                notification.topic,
                notification.schoolId
            ).map {
                it.userId
            }

            notificationOfUserPort.saveNotificationsOfUser(
                userIds.map { notification.toNotificationOfUser(it) }
            )
        }
        notificationPort.sendByTopic(
            notification = notification
        )
    }

    override fun toggleSubscription(token: String, topic: Topic) {
        val deviceToken = this.getDeviceTokenByToken(token)
        val currentSubscribe = queryTopicSubscriptionPort.queryDeviceTokenIdAndTopic(deviceToken.id, topic)

        currentSubscribe?.let {
            if (it.isSubscribed) {
                notificationPort.unsubscribeTopic(token, topic)
            } else {
                notificationPort.subscribeTopic(token, topic)
            }

            commandTopicSubscriptionPort.saveTopicSubscription(
                TopicSubscription(
                    deviceTokenId = deviceToken.id,
                    topic = topic,
                    isSubscribed = !it.isSubscribed
                )
            )
        }
    }
}
