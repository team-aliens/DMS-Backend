package team.aliens.dms.thirdparty.rabbitmq

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component
import team.aliens.dms.GroupNotificationInfo
import team.aliens.dms.SingleNotificationInfo
import team.aliens.dms.domain.notification.spi.NotificationPort

@Component
class RabbitMqAdapter(
    private val rabbitTemplate: RabbitTemplate,
) : NotificationPort {

    override fun sendSingleNotification(info: SingleNotificationInfo) {
        rabbitTemplate.convertAndSend("dms.notification.exchange", "notification.single", info)
    }

    override fun sendGroupNotification(info: GroupNotificationInfo) {
        rabbitTemplate.convertAndSend("dms.notification.exchange", "notification.group", info)
    }

    override fun sendTopicNotification(topic: String, info: Any) {
        rabbitTemplate.convertAndSend("dms.notification.exchange", "notification.$topic", info)
    }
}
