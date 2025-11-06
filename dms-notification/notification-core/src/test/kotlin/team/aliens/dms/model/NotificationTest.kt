package team.aliens.dms.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import team.aliens.dms.stub.createNotificationStub

class NotificationTest : DescribeSpec({
        describe("IsSaveRequiredTest") {

            var called = false

            context("저장 요청이 true 라면") {
                val notification = createNotificationStub(isSaveRequired = true)

                it("함수를 실행시킨다") {
                    notification.runIfSaveRequired {
                        called = true
                    }
                    called shouldBe true
                }
            }

            context("저장 요청이 false 라면") {
                val notification = createNotificationStub(isSaveRequired = false)
                called = false

                it("함수를 실행시키지 않는다.") {
                    notification.runIfSaveRequired {
                        called = true
                    }
                    called shouldBe false
                }
            }
        }
    }
)