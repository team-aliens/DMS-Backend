package team.aliens.dms.domain.notification.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import team.aliens.dms.contract.model.notification.Topic
import team.aliens.dms.domain.notification.exception.DeviceTokenNotFoundException
import team.aliens.dms.domain.notification.exception.NotificationOfUserNotFoundException
import team.aliens.dms.domain.notification.spi.QueryDeviceTokenPort
import team.aliens.dms.domain.notification.spi.QueryNotificationOfUserPort
import team.aliens.dms.domain.notification.spi.QueryTopicSubscriptionPort
import team.aliens.dms.domain.notification.stub.createDeviceTokenStub
import team.aliens.dms.domain.notification.stub.createNotificationOfUserStub
import team.aliens.dms.domain.notification.stub.createTopicSubscriptionStub
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
                createNotificationOfUserStub(userId = userId, topic = Topic.NOTICE),
                createNotificationOfUserStub(userId = userId, topic = Topic.POINT)
            )

            every { notificationOfUserPort.queryNotificationOfUserByUserId(userId) } returns notifications

            val result = service.getNotificationOfUsersByUserId(userId)

            it("알림 목록을 반환한다") {
                result shouldBe notifications
            }
        }
    }

    describe("getNotificationOfUserById") {
        context("알림 ID로 알림을 조회하면") {
            val notification = createNotificationOfUserStub()

            every { notificationOfUserPort.queryNotificationOfUserById(notification.id) } returns notification

            val result = service.getNotificationOfUserById(notification.id)

            it("알림을 반환한다") {
                result shouldBe notification
            }
        }

        context("존재하지 않는 알림 ID로 조회하면") {
            val notificationId = UUID.randomUUID()

            every { notificationOfUserPort.queryNotificationOfUserById(notificationId) } returns null

            it("NotificationOfUserNotFoundException을 던진다") {
                shouldThrow<NotificationOfUserNotFoundException> {
                    service.getNotificationOfUserById(notificationId)
                }
            }
        }
    }

    describe("getTopicSubscriptionsByToken") {
        context("토큰으로 주제 구독 목록을 조회하면") {
            val deviceToken = createDeviceTokenStub(token = "subscriptions-token")
            val subscriptions = listOf(
                createTopicSubscriptionStub(deviceTokenId = deviceToken.id, topic = Topic.NOTICE),
                createTopicSubscriptionStub(deviceTokenId = deviceToken.id, topic = Topic.POINT)
            )

            every { deviceTokenPort.queryDeviceTokenByToken(deviceToken.token) } returns deviceToken
            every { topicSubscriptionPort.queryTopicSubscriptionsByDeviceTokenId(deviceToken.id) } returns subscriptions

            val result = service.getTopicSubscriptionsByToken(deviceToken.token)

            it("주제 구독 목록을 반환한다") {
                result shouldBe subscriptions
            }
        }
    }

    describe("getDeviceTokenByToken") {
        context("토큰으로 디바이스 토큰을 조회하면") {
            val deviceToken = createDeviceTokenStub(token = "existing-token")

            every { deviceTokenPort.queryDeviceTokenByToken(deviceToken.token) } returns deviceToken

            val result = service.getDeviceTokenByToken(deviceToken.token)

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
            val deviceToken = createDeviceTokenStub()

            every { deviceTokenPort.queryDeviceTokenByUserId(deviceToken.userId) } returns deviceToken

            val result = service.getDeviceTokenByUserId(deviceToken.userId)

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

    describe("getDeviceTokensByUserIds") {
        context("여러 사용자 ID로 디바이스 토큰을 조회하면") {
            val deviceTokens = listOf(createDeviceTokenStub(token = "token1"), createDeviceTokenStub(token = "token2"))
            val userIds = deviceTokens.map { it.userId }

            every { deviceTokenPort.queryDeviceTokensByUserIds(userIds) } returns deviceTokens

            val result = service.getDeviceTokensByUserIds(userIds)

            it("디바이스 토큰 목록을 반환한다") {
                result shouldBe deviceTokens
            }
        }
    }
})
