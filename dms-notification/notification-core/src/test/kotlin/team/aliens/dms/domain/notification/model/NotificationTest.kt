package team.aliens.dms.domain.notification.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import io.mockk.verify
import team.aliens.dms.contract.model.notification.Topic
import java.util.UUID

class NotificationTest : DescribeSpec({

    describe("runIfSaveRequired") {
        context("isSaveRequired가 true이면") {
            val notification = Notification(
                schoolId = UUID.randomUUID(),
                topic = Topic.NOTICE,
                linkIdentifier = null,
                title = "title",
                content = "content",
                threadId = "thread-id",
                isSaveRequired = true
            )

            it("함수가 실행된다") {
                var executed = false
                notification.runIfSaveRequired {
                    executed = true
                }
                executed shouldBe true
            }
        }

        context("isSaveRequired가 false이면") {
            val notification = Notification(
                schoolId = UUID.randomUUID(),
                topic = Topic.NOTICE,
                linkIdentifier = null,
                title = "title",
                content = "content",
                threadId = "thread-id",
                isSaveRequired = false
            )

            it("함수가 실행되지 않는다") {
                var executed = false
                notification.runIfSaveRequired {
                    executed = true
                }
                executed shouldBe false
            }
        }
    }

    describe("toNotificationOfUser") {
        context("Notification을 NotificationOfUser로 변환하면") {
            val userId = UUID.randomUUID()
            val notification = Notification(
                schoolId = UUID.randomUUID(),
                topic = Topic.NOTICE,
                linkIdentifier = "link-id",
                title = "title",
                content = "content",
                threadId = "thread-id",
                isSaveRequired = true
            )

            val result = notification.toNotificationOfUser(userId)

            it("올바른 NotificationOfUser를 반환한다") {
                result.userId shouldBe userId
                result.topic shouldBe notification.topic
                result.linkIdentifier shouldBe notification.linkIdentifier
                result.title shouldBe notification.title
                result.content shouldBe notification.content
                result.createdAt shouldBe notification.createdAt
            }
        }
    }
})
