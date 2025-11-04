package team.aliens.dms.thirdparty.messagebroker.config

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
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
import team.aliens.dms.thirdparty.messagebroker.properties.RabbitMqProperties

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
        return TopicExchange("notification_exchange")
    }

    @Bean
    fun notificationQueue(): Queue {
        return Queue("notification_queue")
    }

    @Bean
    fun notificationBinding(
        notificationQueue: Queue,
        notificationExchange: TopicExchange,
    ): Binding {
        return BindingBuilder.bind(notificationQueue).to(notificationExchange).with("notification")
    }

    @Bean
    fun deviceTokenQueue(): Queue {
        return Queue("device_token_queue")
    }

    @Bean
    fun deviceTokenBinding(
        deviceTokenQueue: Queue,
        notificationExchange: TopicExchange,
    ): Binding {
        return BindingBuilder.bind(deviceTokenQueue).to(notificationExchange).with("device_token")
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
    fun messageConverter(): Jackson2JsonMessageConverter {
        val mapper = ObjectMapper()
            .registerModule(JavaTimeModule())
            .registerKotlinModule()
            .activateDefaultTyping(
                LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
            )
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

        return Jackson2JsonMessageConverter(mapper)
    }
}
