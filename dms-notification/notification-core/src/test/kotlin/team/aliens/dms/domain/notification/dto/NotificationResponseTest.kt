package team.aliens.dms.domain.notification.dto

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import team.aliens.dms.stub.createNotificationOfUserStub

class NotificationResponseTest : DescribeSpec({

    describe("NotificationResponse.of") {
        context("NotificationOfUser로부터 NotificationResponse를 생성하면") {
            val notificationOfUser = createNotificationOfUserStub()

            it("NotificationResponse를 반환한다") {
                val response = NotificationResponse.of(notificationOfUser)

                response.id shouldBe notificationOfUser.id
                response.topic shouldBe notificationOfUser.topic
                response.linkIdentifier shouldBe notificationOfUser.linkIdentifier
                response.title shouldBe notificationOfUser.title
                response.content shouldBe notificationOfUser.content
                response.createdAt shouldBe notificationOfUser.createdAt
                response.isRead shouldBe notificationOfUser.isRead
            }
        }
    }

    describe("NotificationsResponse.of") {
        context("NotificationOfUser 목록으로부터 NotificationsResponse를 생성하면") {
            val notificationOfUsers = listOf(
                createNotificationOfUserStub(),
                createNotificationOfUserStub()
            )

            it("NotificationsResponse를 반환한다") {
                val response = NotificationsResponse.of(notificationOfUsers)

                response.notifications.size shouldBe 2
            }
        }
    }
})
