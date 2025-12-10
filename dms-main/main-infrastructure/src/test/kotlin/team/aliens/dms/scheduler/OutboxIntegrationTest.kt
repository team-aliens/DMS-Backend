package team.aliens.dms.scheduler

import com.fasterxml.jackson.annotation.JsonTypeInfo
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.context.annotation.Primary
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import team.aliens.dms.common.dto.OutboxData
import team.aliens.dms.common.dto.OutboxStatus
import team.aliens.dms.common.spi.OutboxPort
import team.aliens.dms.config.MySQLTestContainer
import team.aliens.dms.config.RabbitMQTestContainer
import team.aliens.dms.contract.model.notification.NotificationInfo
import team.aliens.dms.contract.model.notification.Topic
import team.aliens.dms.contract.remote.rabbitmq.SingleNotificationMessage
import team.aliens.dms.persistence.outbox.mapper.OutboxMapper
import team.aliens.dms.persistence.outbox.repository.OutboxJpaRepository
import java.time.LocalDateTime
import java.util.UUID

@SpringBootTest(
    properties = [
        "spring.flyway.enabled=false",
        "spring.mail.properties.mail.smtp.auth=true",
        "spring.mail.properties.mail.smtp.starttls.enable=true"
    ]
)
@Import(OutboxIntegrationTest.TestConfig::class)
class OutboxIntegrationTest(
    private val outboxPort: OutboxPort,
    private val outboxScheduler: OutboxScheduler,
    private val rabbitTemplate: RabbitTemplate,
    private val objectMapper: ObjectMapper,
    private val outboxJpaRepository: OutboxJpaRepository
) : DescribeSpec() {

    override fun extensions() = listOf(SpringExtension)

    companion object {
        @JvmStatic
        @DynamicPropertySource
        fun configureProperties(registry: DynamicPropertyRegistry) {
            MySQLTestContainer.configure(registry)
            RabbitMQTestContainer.configure(registry)
        }
    }

    @TestConfiguration
    class TestConfig {
        @Bean
        fun outboxMapper() = OutboxMapper()

        @Bean
        @Primary
        fun testObjectMapper(): ObjectMapper {
            return ObjectMapper()
                .registerModule(JavaTimeModule())
                .registerKotlinModule()
                .activateDefaultTyping(
                    LaissezFaireSubTypeValidator.instance,
                    ObjectMapper.DefaultTyping.NON_FINAL,
                    JsonTypeInfo.As.PROPERTY
                )
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        }
    }

    init {
        this.afterEach {
            outboxJpaRepository.deleteAll()
            rabbitTemplate.execute { channel ->
                channel.queuePurge("notification_queue")
                null
            }
        }

        describe("OutboxScheduler 통합 테스트") {
            context("PENDING 상태의 Outbox가 있고 메시지 전송에 성공하면") {
                it("Outbox가 삭제되고 메시지가 RabbitMQ로 전송된다") {
                    val userId = UUID.randomUUID()
                    val notificationInfo = NotificationInfo(
                        schoolId = UUID.randomUUID(),
                        topic = Topic.NOTICE,
                        linkIdentifier = null,
                        title = "테스트 알림",
                        content = "테스트 내용",
                        threadId = "test-thread",
                        isSaveRequired = false
                    )
                    val message = SingleNotificationMessage(
                        userId = userId,
                        notificationInfo = notificationInfo
                    )
                    val payload = objectMapper.writeValueAsString(message)

                    val outbox = OutboxData(
                        id = null,
                        aggregateType = "notification",
                        eventType = SingleNotificationMessage.TYPE,
                        payload = payload,
                        status = OutboxStatus.PENDING,
                        retryCount = 0,
                        createdAt = LocalDateTime.now()
                    )

                    val saved = outboxPort.save(outbox)
                    saved.id shouldNotBe null

                    outboxScheduler.processOutbox()

                    val remaining = outboxPort.findByStatus(OutboxStatus.PENDING)
                    remaining shouldHaveSize 0

                    val messageFromQueue = rabbitTemplate.receive("notification_queue", 3000)
                    messageFromQueue shouldNotBe null

                    val receivedMessage = objectMapper.readValue(
                        messageFromQueue!!.body,
                        SingleNotificationMessage::class.java
                    )
                    receivedMessage.userId shouldBe userId
                    receivedMessage.notificationInfo.title shouldBe "테스트 알림"
                }
            }

            context("메시지 전송에 실패하면") {
                it("재시도 횟수가 증가하고 PENDING 상태로 유지된다") {
                    val invalidPayload = """{"invalid": "data"}"""

                    val outbox = OutboxData(
                        id = null,
                        aggregateType = "notification",
                        eventType = SingleNotificationMessage.TYPE,
                        payload = invalidPayload,
                        status = OutboxStatus.PENDING,
                        retryCount = 0,
                        createdAt = LocalDateTime.now()
                    )

                    outboxPort.save(outbox)

                    outboxScheduler.processOutbox()

                    val pending = outboxPort.findByStatus(OutboxStatus.PENDING)
                    pending shouldHaveSize 1
                    pending[0].retryCount shouldBe 1
                }
            }

            context("재시도 횟수가 최대치를 초과하면") {
                it("FAILED 상태로 변경된다") {
                    val invalidPayload = """{"invalid": "data"}"""

                    val outbox = OutboxData(
                        id = null,
                        aggregateType = "notification",
                        eventType = SingleNotificationMessage.TYPE,
                        payload = invalidPayload,
                        status = OutboxStatus.PENDING,
                        retryCount = 2,
                        createdAt = LocalDateTime.now()
                    )

                    outboxPort.save(outbox)

                    outboxScheduler.processOutbox()

                    val failed = outboxPort.findByStatus(OutboxStatus.FAILED)
                    failed shouldHaveSize 1
                    failed[0].retryCount shouldBe 3
                }
            }

            context("알 수 없는 이벤트 타입이면") {
                it("재시도 횟수가 증가한다") {
                    val outbox = OutboxData(
                        id = null,
                        aggregateType = "notification",
                        eventType = "UnknownEventType",
                        payload = """{"data": "test"}""",
                        status = OutboxStatus.PENDING,
                        retryCount = 0,
                        createdAt = LocalDateTime.now()
                    )

                    outboxPort.save(outbox)

                    outboxScheduler.processOutbox()

                    val pending = outboxPort.findByStatus(OutboxStatus.PENDING)
                    pending shouldHaveSize 1
                    pending[0].retryCount shouldBe 1
                }
            }

            context("여러 PENDING Outbox를 처리하면") {
                it("모든 Outbox가 처리된다") {
                    val userId1 = UUID.randomUUID()
                    val userId2 = UUID.randomUUID()

                    val notificationInfo1 = NotificationInfo(
                        schoolId = UUID.randomUUID(),
                        topic = Topic.NOTICE,
                        linkIdentifier = null,
                        title = "알림 1",
                        content = "내용 1",
                        threadId = "thread-1",
                        isSaveRequired = false
                    )
                    val notificationInfo2 = NotificationInfo(
                        schoolId = UUID.randomUUID(),
                        topic = Topic.NOTICE,
                        linkIdentifier = null,
                        title = "알림 2",
                        content = "내용 2",
                        threadId = "thread-2",
                        isSaveRequired = false
                    )

                    val message1 = SingleNotificationMessage(userId1, notificationInfo1)
                    val message2 = SingleNotificationMessage(userId2, notificationInfo2)

                    val outbox1 = OutboxData(
                        id = null,
                        aggregateType = "notification",
                        eventType = SingleNotificationMessage.TYPE,
                        payload = objectMapper.writeValueAsString(message1),
                        status = OutboxStatus.PENDING,
                        retryCount = 0,
                        createdAt = LocalDateTime.now()
                    )
                    val outbox2 = OutboxData(
                        id = null,
                        aggregateType = "notification",
                        eventType = SingleNotificationMessage.TYPE,
                        payload = objectMapper.writeValueAsString(message2),
                        status = OutboxStatus.PENDING,
                        retryCount = 0,
                        createdAt = LocalDateTime.now()
                    )

                    outboxPort.save(outbox1)
                    outboxPort.save(outbox2)

                    outboxScheduler.processOutbox()

                    val remaining = outboxPort.findByStatus(OutboxStatus.PENDING)
                    remaining shouldHaveSize 0

                    val message1FromQueue = rabbitTemplate.receive("notification_queue", 3000)
                    val message2FromQueue = rabbitTemplate.receive("notification_queue", 3000)

                    message1FromQueue shouldNotBe null
                    message2FromQueue shouldNotBe null
                }
            }
        }
    }
}
