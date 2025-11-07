package team.aliens.dms.domain.notification.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.mockk
import io.mockk.verify
import team.aliens.dms.stub.createNotificationStub
import java.util.UUID

class NotificationTest : DescribeSpec({

    afterTest {
        clearAllMocks()
    }

    describe("toNotificationOfUser") {
        context("사용자 ID를 받으면") {
            val userId = UUID.randomUUID()
            val notification = createNotificationStub(
                title = "Test Title",
                content = "Test Content"
            )

            val result = notification.toNotificationOfUser(userId)

            it("NotificationOfUser로 변환된다") {
                result.userId shouldBe userId
                result.topic shouldBe notification.topic
                result.linkIdentifier shouldBe notification.linkIdentifier
                result.title shouldBe notification.title
                result.content shouldBe notification.content
                result.createdAt shouldBe notification.createdAt
            }
        }
    }

    describe("runIfSaveRequired") {
        context("저장이 필요한 경우") {
            val notification = createNotificationStub(isSaveRequired = true)
            val mockFunction = mockk<() -> Unit>(relaxed = true)

            notification.runIfSaveRequired(mockFunction)

            it("함수가 실행된다") {
                verify(exactly = 1) { mockFunction.invoke() }
            }
        }

        context("저장이 필요하지 않은 경우") {
            val notification = createNotificationStub(isSaveRequired = false)
            val mockFunction = mockk<() -> Unit>(relaxed = true)

            notification.runIfSaveRequired(mockFunction)

            it("함수가 실행되지 않는다") {
                verify(exactly = 0) { mockFunction.invoke() }
            }
        }
    }
})
