package team.aliens.dms.persistence.outbox.repository

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import team.aliens.dms.persistence.config.DatabaseTestConfig
import team.aliens.dms.persistence.outbox.entity.OutboxJpaEntity
import team.aliens.dms.persistence.outbox.entity.OutboxStatus
import java.time.LocalDateTime

class OutboxJpaRepositoryTest(
    @Autowired private val outboxJpaRepository: OutboxJpaRepository
) : DatabaseTestConfig() {

    init {
        this.afterEach {
            outboxJpaRepository.deleteAll()
        }

        describe("save") {
            context("OutboxJpaEntity를 저장하면") {
                val entity = OutboxJpaEntity(
                    id = null,
                    aggregateType = "notification",
                    eventType = "SingleNotificationMessage",
                    payload = """{"userId":"test","notificationInfo":{}}""",
                    status = OutboxStatus.PENDING,
                    retryCount = 0,
                    createdAt = LocalDateTime.now()
                )

                val saved = outboxJpaRepository.save(entity)

                it("ID가 생성되어 저장된다") {
                    saved.id shouldNotBe null
                    saved.aggregateType shouldBe "notification"
                    saved.eventType shouldBe "SingleNotificationMessage"
                    saved.status shouldBe OutboxStatus.PENDING
                    saved.retryCount shouldBe 0
                }
            }
        }

        describe("findByStatus") {
            context("여러 상태의 Outbox가 있을 때") {
                val pendingEntity1 = OutboxJpaEntity(
                    id = null,
                    aggregateType = "notification",
                    eventType = "SingleNotificationMessage",
                    payload = """{"userId":"test1"}""",
                    status = OutboxStatus.PENDING,
                    retryCount = 0,
                    createdAt = LocalDateTime.now()
                )
                val pendingEntity2 = OutboxJpaEntity(
                    id = null,
                    aggregateType = "notification",
                    eventType = "GroupNotificationMessage",
                    payload = """{"userIds":["test2"]}""",
                    status = OutboxStatus.PENDING,
                    retryCount = 1,
                    createdAt = LocalDateTime.now()
                )
                val processedEntity = OutboxJpaEntity(
                    id = null,
                    aggregateType = "notification",
                    eventType = "TopicNotificationMessage",
                    payload = """{"topic":"notice"}""",
                    status = OutboxStatus.PROCESSED,
                    retryCount = 0,
                    createdAt = LocalDateTime.now()
                )
                val failedEntity = OutboxJpaEntity(
                    id = null,
                    aggregateType = "notification",
                    eventType = "SingleNotificationMessage",
                    payload = """{"userId":"test4"}""",
                    status = OutboxStatus.FAILED,
                    retryCount = 3,
                    createdAt = LocalDateTime.now()
                )

                outboxJpaRepository.save(pendingEntity1)
                outboxJpaRepository.save(pendingEntity2)
                outboxJpaRepository.save(processedEntity)
                outboxJpaRepository.save(failedEntity)

                it("PENDING 상태만 조회된다") {
                    val pendingResults = outboxJpaRepository.findByStatus(OutboxStatus.PENDING)
                    pendingResults.size shouldBe 2
                    pendingResults.all { it.status == OutboxStatus.PENDING } shouldBe true
                }

                it("PROCESSED 상태만 조회된다") {
                    val processedResults = outboxJpaRepository.findByStatus(OutboxStatus.PROCESSED)
                    processedResults.size shouldBe 1
                    processedResults[0].status shouldBe OutboxStatus.PROCESSED
                }

                it("FAILED 상태만 조회된다") {
                    val failedResults = outboxJpaRepository.findByStatus(OutboxStatus.FAILED)
                    failedResults.size shouldBe 1
                    failedResults[0].status shouldBe OutboxStatus.FAILED
                    failedResults[0].retryCount shouldBe 3
                }
            }
        }

        describe("delete") {
            context("저장된 Outbox를 삭제하면") {
                val entity = OutboxJpaEntity(
                    id = null,
                    aggregateType = "device_token",
                    eventType = "SaveDeviceTokenMessage",
                    payload = """{"token":"fcm-token"}""",
                    status = OutboxStatus.PENDING,
                    retryCount = 0,
                    createdAt = LocalDateTime.now()
                )

                val saved = outboxJpaRepository.save(entity)
                outboxJpaRepository.delete(saved)

                val allEntities = outboxJpaRepository.findAll()

                it("데이터베이스에서 삭제된다") {
                    allEntities.size shouldBe 0
                }
            }
        }

        describe("deleteById") {
            context("ID로 Outbox를 삭제하면") {
                val entity = OutboxJpaEntity(
                    id = null,
                    aggregateType = "device_token",
                    eventType = "DeleteDeviceTokenMessage",
                    payload = """{"userId":"user123"}""",
                    status = OutboxStatus.PENDING,
                    retryCount = 0,
                    createdAt = LocalDateTime.now()
                )

                val saved = outboxJpaRepository.save(entity)
                outboxJpaRepository.deleteById(saved.id!!)

                val exists = outboxJpaRepository.existsById(saved.id!!)

                it("데이터베이스에서 삭제된다") {
                    exists shouldBe false
                }
            }
        }

        describe("재시도 카운트 업데이트") {
            context("재시도 횟수를 증가시키면") {
                val entity = OutboxJpaEntity(
                    id = null,
                    aggregateType = "notification",
                    eventType = "SingleNotificationMessage",
                    payload = """{"userId":"test"}""",
                    status = OutboxStatus.PENDING,
                    retryCount = 0,
                    createdAt = LocalDateTime.now()
                )

                val saved = outboxJpaRepository.save(entity)
                val updated = saved.copy(retryCount = saved.retryCount + 1)
                outboxJpaRepository.save(updated)

                val found = outboxJpaRepository.findById(saved.id!!).get()

                it("재시도 횟수가 업데이트된다") {
                    found.retryCount shouldBe 1
                }
            }
        }

        describe("상태 변경") {
            context("PENDING에서 FAILED로 상태를 변경하면") {
                val entity = OutboxJpaEntity(
                    id = null,
                    aggregateType = "notification",
                    eventType = "SingleNotificationMessage",
                    payload = """{"userId":"test"}""",
                    status = OutboxStatus.PENDING,
                    retryCount = 2,
                    createdAt = LocalDateTime.now()
                )

                val saved = outboxJpaRepository.save(entity)
                val updated = saved.copy(status = OutboxStatus.FAILED, retryCount = 3)
                outboxJpaRepository.save(updated)

                val found = outboxJpaRepository.findById(saved.id!!).get()

                it("상태가 FAILED로 변경된다") {
                    found.status shouldBe OutboxStatus.FAILED
                    found.retryCount shouldBe 3
                }
            }
        }
    }
}
