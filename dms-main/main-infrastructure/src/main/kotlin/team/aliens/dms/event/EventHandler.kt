package team.aliens.dms.event

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import team.aliens.dms.GroupNotificationInfo
import team.aliens.dms.SingleNotificationInfo
import team.aliens.dms.thirdparty.messagebroker.NotificationProducer

@Component
class NotificationEventHandler(
    private val notificationProducer: NotificationProducer
) {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onNotificationEvent(event: TopicNotificationEvent) {
        event.run {
            when (this) {
                is SingleNotificationEvent -> {
                    notificationProducer.sendMessage(
                        SingleNotificationInfo(
                            userId = userId,
                            notificationInfo = this.notificationInfo
                        )
                    )
                }

                is GroupNotificationEvent -> {
                    notificationProducer.sendMessage(
                        GroupNotificationInfo(
                            userIds = userIds,
                            notificationInfo = this.notificationInfo
                        )
                    )
                }

                else -> {
                    notificationProducer.sendMessage(this.notificationInfo)
                }
            }
        }
    }
}
