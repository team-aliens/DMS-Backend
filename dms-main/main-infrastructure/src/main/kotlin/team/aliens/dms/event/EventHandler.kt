package team.aliens.dms.event

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import team.aliens.dms.GroupNotificationInfo
import team.aliens.dms.SingleNotificationInfo

@Component
class NotificationEventHandler(
    private val rabbitTemplate: RabbitTemplate
) {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onNotificationEvent(event: TopicNotificationEvent) {
        event.run {
            when (this) {
                is SingleNotificationEvent -> {
                    rabbitTemplate.convertAndSend("exchange","notification.single",
                        SingleNotificationInfo(
                            userId = userId,
                            notificationInfo = this.notificationInfo
                        )
                    )
                }

                is GroupNotificationEvent -> {
                    rabbitTemplate.convertAndSend("exchange","notification.group",
                        GroupNotificationInfo(
                            userIds = userIds,
                            notificationInfo = this.notificationInfo
                        )
                    )
                }

                else -> {
                    rabbitTemplate.convertAndSend("exchange","notification.topic",
                        this.notificationInfo
                    )
                }
            }
        }
    }
}
