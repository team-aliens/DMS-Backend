package team.aliens.dms.domain.notification.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import team.aliens.dms.contract.model.notification.Topic
import java.util.UUID

class TopicSubscriptionModelTest : DescribeSpec({

    describe("TopicSubscription.subscribe") {
        context("주제를 구독하는 TopicSubscription을 생성하면") {
            val deviceTokenId = UUID.randomUUID()
            val topic = Topic.NOTICE

            it("isSubscribed가 true인 TopicSubscription을 반환한다") {
                val subscription = TopicSubscription.subscribe(deviceTokenId, topic)

                subscription.deviceTokenId shouldBe deviceTokenId
                subscription.topic shouldBe topic
                subscription.isSubscribed shouldBe true
            }
        }
    }

    describe("TopicSubscription.unsubscribe") {
        context("주제 구독을 해제하는 TopicSubscription을 생성하면") {
            val deviceTokenId = UUID.randomUUID()
            val topic = Topic.NOTICE

            it("isSubscribed가 false인 TopicSubscription을 반환한다") {
                val subscription = TopicSubscription.unsubscribe(deviceTokenId, topic)

                subscription.deviceTokenId shouldBe deviceTokenId
                subscription.topic shouldBe topic
                subscription.isSubscribed shouldBe false
            }
        }
    }
})
