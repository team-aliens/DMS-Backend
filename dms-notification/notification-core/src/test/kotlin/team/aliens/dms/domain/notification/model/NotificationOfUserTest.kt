package team.aliens.dms.domain.notification.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import team.aliens.dms.contract.model.notification.Topic
import java.time.LocalDateTime
import java.util.UUID

class NotificationOfUserTest : DescribeSpec({

    describe("read") {
        context("읽지 않은 알림을 읽으면") {
            val notificationOfUser = NotificationOfUser(
                id = UUID.randomUUID(),
                userId = UUID.randomUUID(),
                topic = Topic.NOTICE,
                linkIdentifier = null,
                title = "title",
                content = "content",
                createdAt = LocalDateTime.now(),
                isRead = false
            )

            val result = notificationOfUser.read()

            it("isRead가 true로 변경된다") {
                result.isRead shouldBe true
            }

            it("다른 필드는 유지된다") {
                result.id shouldBe notificationOfUser.id
                result.userId shouldBe notificationOfUser.userId
                result.topic shouldBe notificationOfUser.topic
                result.linkIdentifier shouldBe notificationOfUser.linkIdentifier
                result.title shouldBe notificationOfUser.title
                result.content shouldBe notificationOfUser.content
                result.createdAt shouldBe notificationOfUser.createdAt
            }
        }

        context("이미 읽은 알림을 읽으면") {
            val notificationOfUser = NotificationOfUser(
                id = UUID.randomUUID(),
                userId = UUID.randomUUID(),
                topic = Topic.NOTICE,
                linkIdentifier = null,
                title = "title",
                content = "content",
                createdAt = LocalDateTime.now(),
                isRead = true
            )

            val result = notificationOfUser.read()

            it("isRead가 true로 유지된다") {
                result.isRead shouldBe true
            }
        }
    }
})
