package team.aliens.dms.persistence.outbox.mapper

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import team.aliens.dms.common.dto.OutboxData
import team.aliens.dms.common.dto.OutboxStatus
import team.aliens.dms.persistence.outbox.entity.OutboxJpaEntity
import team.aliens.dms.persistence.outbox.entity.OutboxStatus as JpaOutboxStatus
import java.time.LocalDateTime
import java.util.UUID

class OutboxMapperTest : DescribeSpec({

    val mapper = OutboxMapper()

    describe("toDomain") {
        context("OutboxJpaEntity를 OutboxData로 변환하면") {
            val id = UUID.randomUUID()
            val createdAt = LocalDateTime.now()
            val entity = OutboxJpaEntity(
                id = id,
                aggregateType = "notification",
                eventType = "SingleNotificationMessage",
                payload = """{"userId":"test","notificationInfo":{}}""",
                status = JpaOutboxStatus.PENDING,
                retryCount = 0,
                createdAt = createdAt,
                processedAt = null
            )

            val result = mapper.toDomain(entity)

            it("올바르게 변환된다") {
                result.id shouldBe id
                result.aggregateType shouldBe "notification"
                result.eventType shouldBe "SingleNotificationMessage"
                result.status shouldBe OutboxStatus.PENDING
                result.retryCount shouldBe 0
                result.createdAt shouldBe createdAt
            }
        }
    }

    describe("toEntity") {
        context("OutboxData를 OutboxJpaEntity로 변환하면") {
            val id = UUID.randomUUID()
            val createdAt = LocalDateTime.now()
            val domain = OutboxData(
                id = id,
                aggregateType = "notification",
                eventType = "SingleNotificationMessage",
                payload = """{"userId":"test","notificationInfo":{}}""",
                status = OutboxStatus.PENDING,
                retryCount = 0,
                createdAt = createdAt,
                processedAt = null
            )

            val result = mapper.toEntity(domain)

            it("올바르게 변환된다") {
                result.id shouldBe id
                result.aggregateType shouldBe "notification"
                result.eventType shouldBe "SingleNotificationMessage"
                result.status shouldBe JpaOutboxStatus.PENDING
                result.retryCount shouldBe 0
                result.createdAt shouldBe createdAt
            }
        }
    }

    describe("toJpaStatus") {
        context("OutboxStatus.PENDING을 변환하면") {
            val result = mapper.toJpaStatus(OutboxStatus.PENDING)

            it("JpaOutboxStatus.PENDING으로 변환된다") {
                result shouldBe JpaOutboxStatus.PENDING
            }
        }

        context("OutboxStatus.PROCESSED를 변환하면") {
            val result = mapper.toJpaStatus(OutboxStatus.PROCESSED)

            it("JpaOutboxStatus.PROCESSED로 변환된다") {
                result shouldBe JpaOutboxStatus.PROCESSED
            }
        }

        context("OutboxStatus.FAILED를 변환하면") {
            val result = mapper.toJpaStatus(OutboxStatus.FAILED)

            it("JpaOutboxStatus.FAILED로 변환된다") {
                result shouldBe JpaOutboxStatus.FAILED
            }
        }
    }
})
