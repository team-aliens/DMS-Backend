package team.aliens.dms.domain.notification.dto

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import team.aliens.dms.contract.model.notification.Topic
import team.aliens.dms.stub.createTopicSubscriptionStub

class TopicSubscriptionResponseTest : DescribeSpec({

    describe("TopicSubscriptionGroupsResponse.of") {
        context("TopicSubscription 목록으로부터 TopicSubscriptionGroupsResponse를 생성하면") {
            val topicSubscriptions = listOf(
                createTopicSubscriptionStub(topic = Topic.NOTICE, isSubscribed = true),
                createTopicSubscriptionStub(topic = Topic.POINT, isSubscribed = false)
            )

            it("TopicSubscriptionGroupsResponse를 반환한다") {
                val response = TopicSubscriptionGroupsResponse.of(topicSubscriptions)

                response.topicGroups.size shouldBe 4
            }
        }
    }
})
