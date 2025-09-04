package team.aliens.dms.event

import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import team.aliens.dms.GroupNotificationInfo
import team.aliens.dms.SingleNotificationInfo
import team.aliens.dms.thirdparty.rabbitmq.port.RabbitMqPort

@Component
class NotificationEventHandler(
    private val rabbitMqPort: RabbitMqPort
) {

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onNotificationEvent(event: TopicNotificationEvent) {
        event.run {
            when (this) {
                is SingleNotificationEvent -> {
                    rabbitMqPort.sendMessage("exchange","notification.single",
                        SingleNotificationInfo(
                            userId = userId,
                            notificationInfo = this.notificationInfo
                        )
                    )
                }

                is GroupNotificationEvent -> {
                    rabbitMqPort.sendMessage("exchange","notification.group",
                        GroupNotificationInfo(
                            userIds = userIds,
                            notificationInfo = this.notificationInfo
                        )
                    )
                }

                else -> {
                    rabbitMqPort.sendMessage("exchange","notification.topic",
                        this.notificationInfo
                    )
                }
            }
        }
    }
}
