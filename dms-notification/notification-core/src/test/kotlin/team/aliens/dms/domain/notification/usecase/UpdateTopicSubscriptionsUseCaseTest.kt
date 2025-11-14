package team.aliens.dms.domain.notification.usecase

import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import team.aliens.dms.contract.model.notification.Topic
import team.aliens.dms.domain.notification.service.NotificationService

class UpdateTopicSubscriptionsUseCaseTest : DescribeSpec({

    val notificationService = mockk<NotificationService>()

    val updateTopicSubscriptionsUseCase = UpdateTopicSubscriptionsUseCase(
        notificationService = notificationService
    )

    describe("execute") {
        context("주제 구독 정보를 업데이트하면") {
            val token = "test-device-token"
            val topicsToSubscribe = listOf(
                Pair(Topic.NOTICE, true),
                Pair(Topic.POINT, false)
            )

            every { notificationService.updateSubscribes(token, topicsToSubscribe) } returns Unit

            it("주제 구독 정보를 업데이트한다") {
                updateTopicSubscriptionsUseCase.execute(token, topicsToSubscribe)

                verify(exactly = 1) { notificationService.updateSubscribes(token, topicsToSubscribe) }
            }
        }
    }
})
