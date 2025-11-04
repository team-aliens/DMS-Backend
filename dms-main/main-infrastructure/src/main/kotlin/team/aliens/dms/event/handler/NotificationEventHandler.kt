package team.aliens.dms.event.handler

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import team.aliens.dms.contract.remote.rabbitmq.GroupNotificationMessage
import team.aliens.dms.contract.remote.rabbitmq.SingleNotificationMessage
import team.aliens.dms.contract.remote.rabbitmq.TopicNotificationMessage
import team.aliens.dms.event.GroupNotificationEvent
import team.aliens.dms.event.NotificationEvent
import team.aliens.dms.event.SingleNotificationEvent
import team.aliens.dms.event.TopicNotificationEvent
import team.aliens.dms.thirdparty.messagebroker.NotificationProducer

@Component
class NotificationEventHandler(
    private val notificationProducer: NotificationProducer
) {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onNotificationEvent(event: NotificationEvent) {
        when (event) {
            is SingleNotificationEvent -> {
                notificationProducer.sendMessage(
                    SingleNotificationMessage(
                        userId = event.userId,
                        notificationInfo = event.notificationInfo
                    )
                )
            }

            is GroupNotificationEvent -> {
                notificationProducer.sendMessage(
                    GroupNotificationMessage(
                        userIds = event.userIds,
                        notificationInfo = event.notificationInfo
                    )
                )
            }

            is TopicNotificationEvent -> {
                notificationProducer.sendMessage(
                    TopicNotificationMessage(
                        notificationInfo = event.notificationInfo
                    )
                )
            }
        }
    }
}
