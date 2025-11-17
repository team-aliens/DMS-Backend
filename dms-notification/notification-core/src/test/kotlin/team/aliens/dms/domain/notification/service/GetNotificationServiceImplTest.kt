package team.aliens.dms.domain.notification.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import team.aliens.dms.contract.model.notification.Topic
import team.aliens.dms.domain.notification.exception.DeviceTokenNotFoundException
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.model.NotificationOfUser
import team.aliens.dms.domain.notification.model.TopicSubscription
import team.aliens.dms.domain.notification.spi.QueryDeviceTokenPort
import team.aliens.dms.domain.notification.spi.QueryNotificationOfUserPort
import team.aliens.dms.domain.notification.spi.QueryTopicSubscriptionPort
import java.time.LocalDateTime
import java.util.UUID

class GetNotificationServiceImplTest : DescribeSpec({

    val deviceTokenPort = mockk<QueryDeviceTokenPort>()
    val notificationOfUserPort = mockk<QueryNotificationOfUserPort>()
    val topicSubscriptionPort = mockk<QueryTopicSubscriptionPort>()

    val service = GetNotificationServiceImpl(
        deviceTokenPort = deviceTokenPort,
        notificationOfUserPort = notificationOfUserPort,
        topicSubscriptionPort = topicSubscriptionPort
    )

    describe("getNotificationOfUsersByUserId") {
        context("사용자 ID로 알림 목록을 조회하면") {
            val userId = UUID.randomUUID()
            val notifications = listOf(
                NotificationOfUser(
                    id = UUID.randomUUID(),
                    userId = userId,
                    topic = Topic.NOTICE,
                    linkIdentifier = null,
                    title = "title1",
                    content = "content1",
                    createdAt = LocalDateTime.now()
                ),
                NotificationOfUser(
                    id = UUID.randomUUID(),
                    userId = userId,
                    topic = Topic.POINT,
                    linkIdentifier = null,
                    title = "title2",
                    content = "content2",
                    createdAt = LocalDateTime.now()
                )
            )

            every { notificationOfUserPort.queryNotificationOfUserByUserId(userId) } returns notifications

            val result = service.getNotificationOfUsersByUserId(userId)

            it("알림 목록을 반환한다") {
                result shouldBe notifications
            }
        }
    }

    describe("getTopicSubscriptionsByToken") {
        context("토큰으로 주제 구독 목록을 조회하면") {
            val token = "device-token"
            val deviceToken = DeviceToken(
                id = UUID.randomUUID(),
                userId = UUID.randomUUID(),
                schoolId = UUID.randomUUID(),
                token = token
            )
            val subscriptions = listOf(
                TopicSubscription.subscribe(deviceToken.id, Topic.NOTICE),
                TopicSubscription.subscribe(deviceToken.id, Topic.POINT)
            )

            every { deviceTokenPort.queryDeviceTokenByToken(token) } returns deviceToken
            every { topicSubscriptionPort.queryTopicSubscriptionsByDeviceTokenId(deviceToken.id) } returns subscriptions

            val result = service.getTopicSubscriptionsByToken(token)

            it("주제 구독 목록을 반환한다") {
                result shouldBe subscriptions
            }
        }
    }

    describe("getDeviceTokenByToken") {
        context("토큰으로 디바이스 토큰을 조회하면") {
            val token = "device-token"
            val deviceToken = DeviceToken(
                id = UUID.randomUUID(),
                userId = UUID.randomUUID(),
                schoolId = UUID.randomUUID(),
                token = token
            )

            every { deviceTokenPort.queryDeviceTokenByToken(token) } returns deviceToken

            val result = service.getDeviceTokenByToken(token)

            it("디바이스 토큰을 반환한다") {
                result shouldBe deviceToken
            }
        }

        context("존재하지 않는 토큰으로 조회하면") {
            val token = "invalid-token"

            every { deviceTokenPort.queryDeviceTokenByToken(token) } returns null

            it("DeviceTokenNotFoundException을 던진다") {
                shouldThrow<DeviceTokenNotFoundException> {
                    service.getDeviceTokenByToken(token)
                }
            }
        }
    }

    describe("getDeviceTokenByUserId") {
        context("사용자 ID로 디바이스 토큰을 조회하면") {
            val userId = UUID.randomUUID()
            val deviceToken = DeviceToken(
                id = UUID.randomUUID(),
                userId = userId,
                schoolId = UUID.randomUUID(),
                token = "token"
            )

            every { deviceTokenPort.queryDeviceTokenByUserId(userId) } returns deviceToken

            val result = service.getDeviceTokenByUserId(userId)

            it("디바이스 토큰을 반환한다") {
                result shouldBe deviceToken
            }
        }

        context("존재하지 않는 사용자 ID로 조회하면") {
            val userId = UUID.randomUUID()

            every { deviceTokenPort.queryDeviceTokenByUserId(userId) } returns null

            it("DeviceTokenNotFoundException을 던진다") {
                shouldThrow<DeviceTokenNotFoundException> {
                    service.getDeviceTokenByUserId(userId)
                }
            }
        }
    }

    describe("getDiviceTokensByUserIds") {
        context("여러 사용자 ID로 디바이스 토큰을 조회하면") {
            val userIds = listOf(UUID.randomUUID(), UUID.randomUUID())
            val deviceTokens = listOf(
                DeviceToken(
                    id = UUID.randomUUID(),
                    userId = userIds[0],
                    schoolId = UUID.randomUUID(),
                    token = "token1"
                ),
                DeviceToken(
                    id = UUID.randomUUID(),
                    userId = userIds[1],
                    schoolId = UUID.randomUUID(),
                    token = "token2"
                )
            )

            every { deviceTokenPort.queryDeviceTokensByUserIds(userIds) } returns deviceTokens

            val result = service.getDiviceTokensByUserIds(userIds)

            it("디바이스 토큰 목록을 반환한다") {
                result shouldBe deviceTokens
            }
        }
    }
})
