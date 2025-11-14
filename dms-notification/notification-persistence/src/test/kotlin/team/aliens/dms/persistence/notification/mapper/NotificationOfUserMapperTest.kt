package team.aliens.dms.persistence.notification.mapper

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import team.aliens.dms.contract.model.notification.Topic
import team.aliens.dms.domain.notification.model.NotificationOfUser
import team.aliens.dms.persistence.notification.entity.NotificationOfUserJpaEntity
import java.time.LocalDateTime
import java.util.UUID

class NotificationOfUserMapperTest : DescribeSpec({

    val mapper = NotificationOfUserMapper()

    describe("toDomain") {
        context("Entity를 Domain으로 변환하면") {
            val id = UUID.randomUUID()
            val userId = UUID.randomUUID()
            val topic = Topic.NOTICE
            val title = "Test Title"
            val content = "Test Content"
            val createdAt = LocalDateTime.now()

            val entity = NotificationOfUserJpaEntity(
                id = id,
                userId = userId,
                topic = topic,
                linkIdentifier = null,
                title = title,
                content = content,
                createdAt = createdAt,
                isRead = false
            )

            it("NotificationOfUser를 반환한다") {
                val domain = mapper.toDomain(entity)

                domain?.id shouldBe id
                domain?.userId shouldBe userId
                domain?.topic shouldBe topic
                domain?.title shouldBe title
                domain?.content shouldBe content
                domain?.isRead shouldBe false
            }
        }

        context("null Entity를 변환하면") {
            it("null을 반환한다") {
                val domain = mapper.toDomain(null)

                domain shouldBe null
            }
        }
    }

    describe("toEntity") {
        context("Domain을 Entity로 변환하면") {
            val id = UUID.randomUUID()
            val userId = UUID.randomUUID()
            val topic = Topic.NOTICE
            val title = "Test Title"
            val content = "Test Content"
            val createdAt = LocalDateTime.now()

            val domain = NotificationOfUser(
                id = id,
                userId = userId,
                topic = topic,
                linkIdentifier = null,
                title = title,
                content = content,
                createdAt = createdAt,
                isRead = false
            )

            it("NotificationOfUserJpaEntity를 반환한다") {
                val entity = mapper.toEntity(domain)

                entity.id shouldBe id
                entity.userId shouldBe userId
                entity.topic shouldBe topic
                entity.title shouldBe title
                entity.content shouldBe content
                entity.isRead shouldBe false
            }
        }
    }
})
