package team.aliens.dms.thirdparty.rabbitmq.port

interface RabbitMqPort {
    fun sendMessage(exchangeName: String, routingKey: String, message: Any)
}