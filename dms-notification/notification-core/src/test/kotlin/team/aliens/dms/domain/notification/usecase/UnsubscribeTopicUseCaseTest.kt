package team.aliens.dms.domain.notification.usecase

import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import team.aliens.dms.contract.model.notification.Topic
import team.aliens.dms.domain.notification.service.NotificationService

class UnsubscribeTopicUseCaseTest : DescribeSpec({

    val notificationService = mockk<NotificationService>()

    val unsubscribeTopicUseCase = UnsubscribeTopicUseCase(
        notificationService = notificationService
    )

    describe("execute") {
        context("주제 구독을 해제하면") {
            val deviceToken = "test-device-token"
            val topic = Topic.NOTICE

            every { notificationService.unsubscribeTopic(deviceToken, topic) } returns Unit

            it("주제 구독을 해제한다") {
                unsubscribeTopicUseCase.execute(deviceToken, topic)

                verify(exactly = 1) { notificationService.unsubscribeTopic(deviceToken, topic) }
            }
        }
    }
})
