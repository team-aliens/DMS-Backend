package team.aliens.dms.persistence.notification

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import team.aliens.dms.contract.model.notification.Topic
import team.aliens.dms.domain.notification.model.TopicSubscription
import team.aliens.dms.persistence.notification.entity.TopicSubscriptionJpaEntity
import team.aliens.dms.persistence.notification.mapper.TopicSubscriptionMapper
import team.aliens.dms.persistence.notification.repository.TopicSubscriptionJpaRepository
import java.util.UUID

class TopicSubscriptionPersistenceAdapterTest : DescribeSpec({

    val mapper = mockk<TopicSubscriptionMapper>()
    val repository = mockk<TopicSubscriptionJpaRepository>()

    val adapter = TopicSubscriptionPersistenceAdapter(
        topicSubscriptionMapper = mapper,
        topicSubscriptionRepository = repository
    )

    describe("saveTopicSubscription") {
        context("주제 구독 정보를 저장하면") {
            val subscription = TopicSubscription(
                deviceTokenId = UUID.randomUUID(),
                topic = Topic.NOTICE,
                isSubscribed = true
            )
            val entity = mockk<TopicSubscriptionJpaEntity>()

            every { mapper.toEntity(subscription) } returns entity
            every { repository.save(entity) } returns entity
            every { mapper.toDomain(entity) } returns subscription

            it("저장된 TopicSubscription을 반환한다") {
                val result = adapter.saveTopicSubscription(subscription)

                result shouldBe subscription
                verify(exactly = 1) { mapper.toEntity(subscription) }
                verify(exactly = 1) { repository.save(entity) }
            }
        }
    }

    describe("saveAllTopicSubscriptions") {
        context("주제 구독 정보 목록을 저장하면") {
            val subscriptions = listOf(
                TopicSubscription(
                    deviceTokenId = UUID.randomUUID(),
                    topic = Topic.NOTICE,
                    isSubscribed = true
                )
            )
            val entities = mockk<List<TopicSubscriptionJpaEntity>>()

            every { mapper.toEntity(any()) } returns mockk()
            every { repository.saveAll(any<List<TopicSubscriptionJpaEntity>>()) } returns entities

            it("주제 구독 정보를 저장한다") {
                adapter.saveAllTopicSubscriptions(subscriptions)

                verify(exactly = 1) { repository.saveAll(any<List<TopicSubscriptionJpaEntity>>()) }
            }
        }
    }

    describe("deleteAllByDeviceTokenId") {
        context("디바이스 토큰 ID로 모든 주제 구독을 삭제하면") {
            val deviceTokenId = UUID.randomUUID()

            every { repository.deleteAllByDeviceTokenId(deviceTokenId) } returns Unit

            it("주제 구독을 삭제한다") {
                adapter.deleteAllByDeviceTokenId(deviceTokenId)

                verify(exactly = 1) { repository.deleteAllByDeviceTokenId(deviceTokenId) }
            }
        }
    }

    describe("queryTopicSubscriptionsByDeviceTokenId") {
        context("디바이스 토큰 ID로 주제 구독 목록을 조회하면") {
            val deviceTokenId = UUID.randomUUID()
            val subscription = TopicSubscription(
                deviceTokenId = deviceTokenId,
                topic = Topic.NOTICE,
                isSubscribed = true
            )
            val entity = mockk<TopicSubscriptionJpaEntity>()

            every { repository.findByDeviceTokenId(deviceTokenId) } returns listOf(entity)
            every { mapper.toDomain(entity) } returns subscription

            it("주제 구독 목록을 반환한다") {
                val result = adapter.queryTopicSubscriptionsByDeviceTokenId(deviceTokenId)

                result.size shouldBe 1
                verify(exactly = 1) { repository.findByDeviceTokenId(deviceTokenId) }
            }
        }
    }

    describe("queryDeviceTokenIdAndTopic") {
        context("디바이스 토큰 ID와 주제로 구독 정보를 조회하고 존재하면") {
            val deviceTokenId = UUID.randomUUID()
            val topic = Topic.NOTICE
            val subscription = TopicSubscription(
                deviceTokenId = deviceTokenId,
                topic = topic,
                isSubscribed = true
            )
            val entity = mockk<TopicSubscriptionJpaEntity>()

            every { repository.findById_DeviceTokenIdAndId_Topic(deviceTokenId, topic) } returns entity
            every { mapper.toDomain(entity) } returns subscription

            it("TopicSubscription을 반환한다") {
                val result = adapter.queryDeviceTokenIdAndTopic(deviceTokenId, topic)

                result shouldBe subscription
                verify(exactly = 1) { repository.findById_DeviceTokenIdAndId_Topic(deviceTokenId, topic) }
            }
        }
    }
})
