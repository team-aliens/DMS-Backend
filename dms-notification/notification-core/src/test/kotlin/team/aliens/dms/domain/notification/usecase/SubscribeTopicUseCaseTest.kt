package team.aliens.dms.domain.notification.usecase

import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import team.aliens.dms.contract.model.notification.Topic
import team.aliens.dms.domain.notification.service.NotificationService

class SubscribeTopicUseCaseTest : DescribeSpec({

    val notificationService = mockk<NotificationService>()

    val subscribeTopicUseCase = SubscribeTopicUseCase(
        notificationService = notificationService
    )

    describe("execute") {
        context("주제를 구독하면") {
            val deviceToken = "test-device-token"
            val topic = Topic.NOTICE

            every { notificationService.subscribeTopic(deviceToken, topic) } returns Unit

            it("주제를 구독한다") {
                subscribeTopicUseCase.execute(deviceToken, topic)

                verify(exactly = 1) { notificationService.subscribeTopic(deviceToken, topic) }
            }
        }
    }
})
