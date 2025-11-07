package team.aliens.dms.domain.notification.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import team.aliens.dms.stub.createNotificationOfUserStub

class NotificationOfUserTest : DescribeSpec({

    describe("read") {
        context("알림을 읽으면") {
            val notificationOfUser = createNotificationOfUserStub(isRead = false)

            val result = notificationOfUser.read()

            it("isRead가 true로 변경된다") {
                result.isRead shouldBe true
            }
        }

        context("이미 읽은 알림을 다시 읽으면") {
            val notificationOfUser = createNotificationOfUserStub(isRead = true)

            val result = notificationOfUser.read()

            it("isRead가 true로 유지된다") {
                result.isRead shouldBe true
            }
        }
    }
})
