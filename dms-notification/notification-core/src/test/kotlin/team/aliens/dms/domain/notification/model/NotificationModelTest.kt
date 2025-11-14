package team.aliens.dms.domain.notification.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import team.aliens.dms.contract.model.notification.NotificationInfo
import team.aliens.dms.contract.model.notification.Topic
import team.aliens.dms.stub.createNotificationStub
import java.util.UUID

class NotificationModelTest : DescribeSpec({

    describe("Notification.from") {
        context("NotificationInfo로부터 Notification을 생성하면") {
            val schoolId = UUID.randomUUID()
            val topic = Topic.NOTICE
            val title = "Test Title"
            val content = "Test Content"
            val threadId = UUID.randomUUID().toString()

            val notificationInfo = NotificationInfo(
                schoolId = schoolId,
                topic = topic,
                linkIdentifier = null,
                title = title,
                content = content,
                threadId = threadId,
                isSaveRequired = true
            )

            it("Notification을 반환한다") {
                val notification = Notification.from(notificationInfo)

                notification.schoolId shouldBe schoolId
                notification.topic shouldBe topic
                notification.title shouldBe title
                notification.content shouldBe content
                notification.threadId shouldBe threadId
            }
        }
    }

    describe("toNotificationOfUser") {
        context("Notification을 NotificationOfUser로 변환하면") {
            val userId = UUID.randomUUID()
            val notification = createNotificationStub()

            it("NotificationOfUser를 반환한다") {
                val notificationOfUser = notification.toNotificationOfUser(userId)

                notificationOfUser.userId shouldBe userId
                notificationOfUser.topic shouldBe notification.topic
                notificationOfUser.title shouldBe notification.title
                notificationOfUser.content shouldBe notification.content
            }
        }
    }
})
