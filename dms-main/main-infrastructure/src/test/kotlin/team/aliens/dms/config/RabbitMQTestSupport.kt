package team.aliens.dms.config

import org.springframework.test.context.DynamicPropertyRegistry
import org.testcontainers.containers.RabbitMQContainer
import org.testcontainers.utility.DockerImageName

object RabbitMQTestContainer {
    val instance: RabbitMQContainer = RabbitMQContainer(DockerImageName.parse("rabbitmq:3.11-management"))
        .apply {
            start()
        }

    fun configure(registry: DynamicPropertyRegistry) {
        registry.add("spring.rabbitmq.host", instance::getHost)
        registry.add("spring.rabbitmq.port", instance::getAmqpPort)
        registry.add("spring.rabbitmq.username", instance::getAdminUsername)
        registry.add("spring.rabbitmq.password", instance::getAdminPassword)
    }
}
