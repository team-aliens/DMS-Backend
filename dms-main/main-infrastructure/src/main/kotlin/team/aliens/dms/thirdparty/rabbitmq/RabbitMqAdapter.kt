package team.aliens.dms.thirdparty.rabbitmq

import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Component
import team.aliens.dms.thirdparty.rabbitmq.port.RabbitMqPort

@Component
class RabbitMqAdapter(
    private val rabbitTemplate: RabbitTemplate,
) : RabbitMqPort  {
    override fun sendMessage(exchangeName: String, routingKey: String, message: Any) {
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message)
    }

}
