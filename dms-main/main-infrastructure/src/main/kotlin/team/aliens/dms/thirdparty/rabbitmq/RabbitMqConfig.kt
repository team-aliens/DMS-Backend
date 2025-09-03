package team.aliens.dms.thirdparty.rabbitmq

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.Queue
import org.springframework.amqp.core.TopicExchange
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class RabbitMqConfig(
    private val rabbitMqProperties: RabbitMqProperties
) {

    @Bean
    fun connectionFactory(): ConnectionFactory {
        var connectionFactory = CachingConnectionFactory()
        connectionFactory.setHost(rabbitMqProperties.host)
        connectionFactory.setPort(rabbitMqProperties.port)
        connectionFactory.setUsername(rabbitMqProperties.username)
        connectionFactory.setPassword(rabbitMqProperties.password)
        return connectionFactory
    }

    @Bean
    fun notificationExchange(): TopicExchange {
        return TopicExchange("notification.exchange")
    }

    @Bean
    fun notificationQueue(): Queue {
        return Queue("notification.all")
    }

    @Bean
    fun notificationBinding(
        notificationQueue: Queue,
        notificationExchange: TopicExchange,
    ): Binding {
        return BindingBuilder.bind(notificationQueue).to(notificationExchange).with("notification.#")
    }

    @Bean
    fun rabbitTemplate(
        connectionFactory: ConnectionFactory,
        messageConverter: MessageConverter,
    ): RabbitTemplate {
        val rabbitTemplate = RabbitTemplate(connectionFactory)
        rabbitTemplate.messageConverter = messageConverter
        return rabbitTemplate
    }

    @Bean
    fun messageConverter(): MessageConverter {
        return Jackson2JsonMessageConverter()
    }
}
