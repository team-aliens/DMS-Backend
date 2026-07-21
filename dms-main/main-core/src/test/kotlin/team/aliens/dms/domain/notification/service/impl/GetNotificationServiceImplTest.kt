package team.aliens.dms.domain.notification.service.impl

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import team.aliens.dms.domain.notification.exception.DeviceTokenNotFoundException
import team.aliens.dms.domain.notification.service.GetNotificationServiceImpl
import team.aliens.dms.domain.notification.spi.QueryDeviceTokenPort
import team.aliens.dms.domain.notification.spi.QueryNotificationOfUserPort
import team.aliens.dms.domain.notification.spi.QueryTopicSubscriptionPort
import team.aliens.dms.stub.createDeviceTokenStub
import team.aliens.dms.stub.createNotificationOfUserStub
import team.aliens.dms.stub.createTopicSubscriptionStub
import java.util.UUID

class GetNotificationServiceImplTest : DescribeSpec({

    val queryDeviceTokenPort = mockk<QueryDeviceTokenPort>()
    val queryNotificationOfUserPort = mockk<QueryNotificationOfUserPort>()
    val queryTopicSubscriptionPort = mockk<QueryTopicSubscriptionPort>()

    val getNotificationService = GetNotificationServiceImpl(
        deviceTokenPort = queryDeviceTokenPort,
        notificationOfUserPort = queryNotificationOfUserPort,
        topicSubscriptionPort = queryTopicSubscriptionPort
    )

    describe("getNotificationOfUsersByUserId") {
        context("사용자 ID로 알림을 조회하면") {
            val userId = UUID.randomUUID()
            val notifications = listOf(
                createNotificationOfUserStub(userId = userId),
                createNotificationOfUserStub(userId = userId)
            )

            every { queryNotificationOfUserPort.queryNotificationOfUserByUserId(userId) } returns notifications

            it("사용자의 알림 목록을 반환한다") {
                val result = getNotificationService.getNotificationOfUsersByUserId(userId)

                result shouldBe notifications
            }
        }
    }

    describe("getTopicSubscriptionsByToken") {
        context("토큰으로 주제 구독 목록을 조회하면") {
            val token = "test-token"
            val deviceToken = createDeviceTokenStub(token = token)
            val subscriptions = listOf(
                createTopicSubscriptionStub(deviceTokenId = deviceToken.id),
                createTopicSubscriptionStub(deviceTokenId = deviceToken.id)
            )

            every { queryDeviceTokenPort.queryDeviceTokenByToken(token) } returns deviceToken
            every { queryTopicSubscriptionPort.queryTopicSubscriptionsByDeviceTokenId(deviceToken.id) } returns subscriptions

            it("주제 구독 목록을 반환한다") {
                val result = getNotificationService.getTopicSubscriptionsByToken(token)

                result shouldBe subscriptions
            }
        }
    }

    describe("getDeviceTokenByToken") {
        context("토큰으로 디바이스 토큰을 조회하고 존재하면") {
            val token = "test-token"
            val deviceToken = createDeviceTokenStub(token = token)

            every { queryDeviceTokenPort.queryDeviceTokenByToken(token) } returns deviceToken

            it("디바이스 토큰을 반환한다") {
                val result = getNotificationService.getDeviceTokenByToken(token)

                result shouldBe deviceToken
            }
        }

        context("토큰으로 디바이스 토큰을 조회했는데 존재하지 않으면") {
            val token = "non-existent-token"

            every { queryDeviceTokenPort.queryDeviceTokenByToken(token) } returns null

            it("DeviceTokenNotFoundException을 발생시킨다") {
                shouldThrow<DeviceTokenNotFoundException> {
                    getNotificationService.getDeviceTokenByToken(token)
                }
            }
        }
    }

    describe("getDeviceTokenByUserId") {
        context("사용자 ID로 디바이스 토큰을 조회하고 존재하면") {
            val userId = UUID.randomUUID()
            val deviceToken = createDeviceTokenStub(userId = userId)

            every { queryDeviceTokenPort.queryDeviceTokenByUserId(userId) } returns deviceToken

            it("디바이스 토큰을 반환한다") {
                val result = getNotificationService.getDeviceTokenByUserId(userId)

                result shouldBe deviceToken
            }
        }

        context("사용자 ID로 디바이스 토큰을 조회했는데 존재하지 않으면") {
            val userId = UUID.randomUUID()

            every { queryDeviceTokenPort.queryDeviceTokenByUserId(userId) } returns null

            it("DeviceTokenNotFoundException을 발생시킨다") {
                shouldThrow<DeviceTokenNotFoundException> {
                    getNotificationService.getDeviceTokenByUserId(userId)
                }
            }
        }
    }

    describe("getDiviceTokensByUserIds") {
        context("사용자 ID 목록으로 디바이스 토큰 목록을 조회하면") {
            val userIds = listOf(UUID.randomUUID(), UUID.randomUUID())
            val deviceTokens = listOf(
                createDeviceTokenStub(userId = userIds[0]),
                createDeviceTokenStub(userId = userIds[1])
            )

            every { queryDeviceTokenPort.queryDeviceTokensByUserIds(userIds) } returns deviceTokens

            it("디바이스 토큰 목록을 반환한다") {
                val result = getNotificationService.getDiviceTokensByUserIds(userIds)

                result shouldBe deviceTokens
            }
        }
    }
})
