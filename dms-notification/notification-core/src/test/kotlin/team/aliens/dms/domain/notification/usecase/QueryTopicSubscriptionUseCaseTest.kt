package team.aliens.dms.domain.notification.usecase

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import team.aliens.dms.contract.model.notification.Topic
import team.aliens.dms.domain.notification.service.NotificationService
import team.aliens.dms.stub.createTopicSubscriptionStub

class QueryTopicSubscriptionUseCaseTest : DescribeSpec({

    val notificationService = mockk<NotificationService>()

    val queryTopicSubscriptionUseCase = QueryTopicSubscriptionUseCase(
        notificationService = notificationService
    )

    describe("execute") {
        context("주제 구독 정보를 조회하면") {
            val token = "test-device-token"
            val subscriptions = listOf(
                createTopicSubscriptionStub(topic = Topic.NOTICE, isSubscribed = true),
                createTopicSubscriptionStub(topic = Topic.POINT, isSubscribed = true)
            )

            every { notificationService.getTopicSubscriptionsByToken(token) } returns subscriptions
            every { notificationService.updateSubscribes(any(), any()) } returns Unit

            it("주제 구독 정보를 조회하고 누락된 주제를 구독한다") {
                val result = queryTopicSubscriptionUseCase.execute(token)

                result shouldNotBe null
                verify(exactly = 1) { notificationService.getTopicSubscriptionsByToken(token) }
                verify(exactly = 1) { notificationService.updateSubscribes(any(), any()) }
            }
        }
    }
})
