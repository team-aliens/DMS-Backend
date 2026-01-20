package team.aliens.dms.domain.notification.usecase

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import team.aliens.dms.contract.model.notification.Topic
import team.aliens.dms.domain.notification.service.NotificationService

class ToggleSubscriptionUseCaseTest : DescribeSpec({

    val notificationService = mockk<NotificationService>()

    val toggleSubscriptionUseCase = ToggleSubscriptionUseCase(
        notificationService = notificationService
    )

    describe("execute") {
        context("주제 구독을 토글하면") {
            val deviceToken = "test-device-token"
            val topic = Topic.NOTICE

            every { notificationService.toggleSubscription(deviceToken, topic) } returns Unit

            it("주제 구독 상태를 토글한다") {
                shouldNotThrowAny {
                    toggleSubscriptionUseCase.execute(deviceToken, topic)
                }
            }
        }
    }
})
