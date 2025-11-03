package team.aliens.dms.thirdparty.messagebroker

import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component
import team.aliens.dms.DeleteDeviceTokenMessage
import team.aliens.dms.SaveDeviceTokenMessage
import team.aliens.dms.TopicDeviceTokenMessage
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.service.NotificationService

@Component
class DeviceTokenConsumer(
    private val notificationService: NotificationService
) {

    @RabbitListener(queues = ["device_token_queue"])
    fun handleDeviceTokenMessage(message: TopicDeviceTokenMessage) {
        when (message) {
            is SaveDeviceTokenMessage -> {
                notificationService.saveDeviceToken(
                    DeviceToken.from(message.deviceTokenInfo)
                )
            }
            is DeleteDeviceTokenMessage -> {
                notificationService.deleteDeviceTokenByUserId(message.userId)
            }
        }
    }
}
