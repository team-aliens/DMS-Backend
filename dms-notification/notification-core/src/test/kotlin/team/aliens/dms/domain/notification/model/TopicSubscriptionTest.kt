package team.aliens.dms.domain.notification.model

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import team.aliens.dms.contract.model.notification.Topic
import java.util.UUID

class TopicSubscriptionTest : DescribeSpec({

    describe("subscribe") {
        context("deviceTokenId와 topic이 주어지면") {
            it("isSubscribed가 true인 TopicSubscription을 생성한다") {
                val deviceTokenId = UUID.randomUUID()
                val topic = Topic.NOTICE

                val subscription = TopicSubscription.subscribe(deviceTokenId, topic)

                subscription.deviceTokenId shouldBe deviceTokenId
                subscription.topic shouldBe topic
                subscription.isSubscribed shouldBe true
            }
        }

        context("다양한 topic 타입으로 구독하면") {
            it("각 topic에 대해 구독 상태로 생성한다") {
                val deviceTokenId = UUID.randomUUID()

                val noticeSubscription = TopicSubscription.subscribe(deviceTokenId, Topic.NOTICE)
                val studyRoomSubscription = TopicSubscription.subscribe(deviceTokenId, Topic.STUDY_ROOM_TIME_SLOT)
                val pointSubscription = TopicSubscription.subscribe(deviceTokenId, Topic.POINT)
                val outingSubscription = TopicSubscription.subscribe(deviceTokenId, Topic.OUTING)

                noticeSubscription.isSubscribed shouldBe true
                studyRoomSubscription.isSubscribed shouldBe true
                pointSubscription.isSubscribed shouldBe true
                outingSubscription.isSubscribed shouldBe true
            }
        }
    }

    describe("unsubscribe") {
        context("deviceTokenId와 topic이 주어지면") {
            it("isSubscribed가 false인 TopicSubscription을 생성한다") {
                val deviceTokenId = UUID.randomUUID()
                val topic = Topic.NOTICE

                val subscription = TopicSubscription.unsubscribe(deviceTokenId, topic)

                subscription.deviceTokenId shouldBe deviceTokenId
                subscription.topic shouldBe topic
                subscription.isSubscribed shouldBe false
            }
        }

        context("다양한 topic 타입으로 구독 해제하면") {
            it("각 topic에 대해 구독 해제 상태로 생성한다") {
                val deviceTokenId = UUID.randomUUID()

                val noticeUnsubscription = TopicSubscription.unsubscribe(deviceTokenId, Topic.NOTICE)
                val studyRoomUnsubscription = TopicSubscription.unsubscribe(deviceTokenId, Topic.STUDY_ROOM_APPLY)
                val pointUnsubscription = TopicSubscription.unsubscribe(deviceTokenId, Topic.POINT)
                val outingUnsubscription = TopicSubscription.unsubscribe(deviceTokenId, Topic.OUTING)

                noticeUnsubscription.isSubscribed shouldBe false
                studyRoomUnsubscription.isSubscribed shouldBe false
                pointUnsubscription.isSubscribed shouldBe false
                outingUnsubscription.isSubscribed shouldBe false
            }
        }
    }

    describe("구독과 구독 해제 비교") {
        context("같은 deviceTokenId와 topic으로 구독과 구독 해제를 생성하면") {
            it("isSubscribed 값만 다르다") {
                val deviceTokenId = UUID.randomUUID()
                val topic = Topic.POINT

                val subscribed = TopicSubscription.subscribe(deviceTokenId, topic)
                val unsubscribed = TopicSubscription.unsubscribe(deviceTokenId, topic)

                subscribed.deviceTokenId shouldBe unsubscribed.deviceTokenId
                subscribed.topic shouldBe unsubscribed.topic
                subscribed.isSubscribed shouldBe true
                unsubscribed.isSubscribed shouldBe false
            }
        }
    }
})
