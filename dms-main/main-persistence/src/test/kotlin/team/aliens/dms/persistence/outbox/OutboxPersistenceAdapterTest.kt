package team.aliens.dms.persistence.outbox

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import team.aliens.dms.common.dto.OutboxData
import team.aliens.dms.common.dto.OutboxStatus
import team.aliens.dms.persistence.config.DatabaseTestConfig
import team.aliens.dms.persistence.outbox.mapper.OutboxMapper
import team.aliens.dms.persistence.outbox.repository.OutboxJpaRepository
import java.time.LocalDateTime
import java.util.UUID

@Import(OutboxPersistenceAdapterTest.TestConfig::class)
class OutboxPersistenceAdapterTest(
    @Autowired private val outboxPersistenceAdapter: OutboxPersistenceAdapter,
    @Autowired private val outboxJpaRepository: OutboxJpaRepository
) : DatabaseTestConfig() {

    @TestConfiguration
    class TestConfig {
        @Bean
        fun outboxMapper() = OutboxMapper()

        @Bean
        fun outboxPersistenceAdapter(
            outboxJpaRepository: OutboxJpaRepository,
            outboxMapper: OutboxMapper
        ) = OutboxPersistenceAdapter(outboxJpaRepository, outboxMapper)
    }

    init {
        this.afterEach {
            outboxJpaRepository.deleteAll()
        }

        describe("save") {
            context("OutboxData를 저장하면") {
                val outboxData = OutboxData(
                    id = null,
                    aggregateType = "notification",
                    eventType = "SingleNotificationMessage",
                    payload = """{"userId":"test","notificationInfo":{}}""",
                    status = OutboxStatus.PENDING,
                    retryCount = 0,
                    createdAt = LocalDateTime.now()
                )

                val saved = outboxPersistenceAdapter.save(outboxData)

                it("ID가 생성되어 저장된다") {
                    saved.id shouldNotBe null
                    saved.aggregateType shouldBe "notification"
                    saved.eventType shouldBe "SingleNotificationMessage"
                    saved.status shouldBe OutboxStatus.PENDING
                }
            }
        }

        describe("findByStatus") {
            context("PENDING 상태의 Outbox가 있으면") {
                val outboxData1 = OutboxData(
                    id = null,
                    aggregateType = "notification",
                    eventType = "SingleNotificationMessage",
                    payload = """{"userId":"test1"}""",
                    status = OutboxStatus.PENDING,
                    retryCount = 0,
                    createdAt = LocalDateTime.now()
                )
                val outboxData2 = OutboxData(
                    id = null,
                    aggregateType = "notification",
                    eventType = "SingleNotificationMessage",
                    payload = """{"userId":"test2"}""",
                    status = OutboxStatus.PROCESSED,
                    retryCount = 0,
                    createdAt = LocalDateTime.now()
                )

                outboxPersistenceAdapter.save(outboxData1)
                outboxPersistenceAdapter.save(outboxData2)

                val result = outboxPersistenceAdapter.findByStatus(OutboxStatus.PENDING)

                it("PENDING 상태의 Outbox만 조회된다") {
                    result.size shouldBe 1
                    result[0].status shouldBe OutboxStatus.PENDING
                }
            }
        }

        describe("delete") {
            context("Outbox를 삭제하면") {
                val outboxData = OutboxData(
                    id = null,
                    aggregateType = "notification",
                    eventType = "SingleNotificationMessage",
                    payload = """{"userId":"test"}""",
                    status = OutboxStatus.PENDING,
                    retryCount = 0,
                    createdAt = LocalDateTime.now()
                )

                val saved = outboxPersistenceAdapter.save(outboxData)
                outboxPersistenceAdapter.delete(saved)

                val result = outboxJpaRepository.findAll()

                it("Outbox가 삭제된다") {
                    result.size shouldBe 0
                }
            }
        }

        describe("deleteById") {
            context("ID로 Outbox를 삭제하면") {
                val outboxData = OutboxData(
                    id = null,
                    aggregateType = "notification",
                    eventType = "SingleNotificationMessage",
                    payload = """{"userId":"test"}""",
                    status = OutboxStatus.PENDING,
                    retryCount = 0,
                    createdAt = LocalDateTime.now()
                )

                val saved = outboxPersistenceAdapter.save(outboxData)
                outboxPersistenceAdapter.deleteById(saved.id!!)

                val result = outboxJpaRepository.findAll()

                it("Outbox가 삭제된다") {
                    result.size shouldBe 0
                }
            }
        }
    }
}
