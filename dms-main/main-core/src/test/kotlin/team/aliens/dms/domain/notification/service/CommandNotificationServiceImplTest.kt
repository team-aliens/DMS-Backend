package team.aliens.dms.domain.notification.service

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.slot
import io.mockk.verify
import team.aliens.dms.contract.model.notification.Topic
import team.aliens.dms.domain.notification.exception.DeviceTokenNotFoundException
import team.aliens.dms.domain.notification.exception.NotificationOfUserNotFoundException
import team.aliens.dms.domain.notification.model.NotificationOfUser
import team.aliens.dms.domain.notification.model.TopicSubscription
import team.aliens.dms.domain.notification.spi.CommandNotificationOfUserPort
import team.aliens.dms.domain.notification.spi.DeviceTokenPort
import team.aliens.dms.domain.notification.spi.NotificationPort
import team.aliens.dms.domain.notification.spi.QueryNotificationOfUserPort
import team.aliens.dms.domain.notification.spi.TopicSubscriptionPort
import team.aliens.dms.domain.notification.stub.createDeviceTokenStub
import team.aliens.dms.domain.notification.stub.createNotificationOfUserStub
import team.aliens.dms.domain.notification.stub.createNotificationStub
import team.aliens.dms.domain.notification.stub.createTopicSubscriptionStub
import java.time.LocalDateTime
import java.util.UUID

class CommandNotificationServiceImplTest : DescribeSpec({

    val deviceTokenPort = mockk<DeviceTokenPort>()
    val notificationPort = mockk<NotificationPort>()
    val queryNotificationOfUserPort = mockk<QueryNotificationOfUserPort>()
    val commandNotificationOfUserPort = mockk<CommandNotificationOfUserPort>()
    val topicSubscriptionPort = mockk<TopicSubscriptionPort>()

    val service = CommandNotificationServiceImpl(
        deviceTokenPort = deviceTokenPort,
        notificationPort = notificationPort,
        queryNotificationOfUserPort = queryNotificationOfUserPort,
        commandNotificationOfUserPort = commandNotificationOfUserPort,
        topicSubscriptionPort = topicSubscriptionPort
    )

    every { topicSubscriptionPort.saveTopicSubscription(any()) } answers { firstArg() }

    describe("saveDeviceToken") {
        context("디바이스 토큰을 저장하면") {
            val deviceToken = createDeviceTokenStub()

            every { deviceTokenPort.saveDeviceToken(deviceToken) } returns deviceToken
            every { notificationPort.subscribeAllTopics(deviceToken.token) } just runs

            val result = service.saveDeviceToken(deviceToken)

            it("디바이스 토큰을 저장하고 반환한다") {
                result shouldBe deviceToken
            }

            it("모든 주제를 구독한다") {
                verify(exactly = 1) { notificationPort.subscribeAllTopics(deviceToken.token) }
            }
        }
    }

    describe("deleteDeviceTokenByUserId") {
        context("사용자 ID로 디바이스 토큰을 삭제하면") {
            val userId = UUID.randomUUID()
            val deviceToken = createDeviceTokenStub(userId = userId)

            every { deviceTokenPort.queryDeviceTokenByUserId(userId) } returns deviceToken
            every { topicSubscriptionPort.deleteAllByDeviceTokenId(deviceToken.id) } just runs
            every { deviceTokenPort.deleteDeviceTokenByUserId(userId) } just runs

            service.deleteDeviceTokenByUserId(userId)

            it("주제 구독을 모두 삭제한다") {
                verify(exactly = 1) { topicSubscriptionPort.deleteAllByDeviceTokenId(deviceToken.id) }
            }

            it("디바이스 토큰을 삭제한다") {
                verify(exactly = 1) { deviceTokenPort.deleteDeviceTokenByUserId(userId) }
            }
        }

        context("존재하지 않는 사용자 ID로 삭제하면") {
            val userId = UUID.randomUUID()

            every { deviceTokenPort.queryDeviceTokenByUserId(userId) } returns null

            it("예외 없이 아무 동작도 하지 않는다") {
                shouldNotThrowAny {
                    service.deleteDeviceTokenByUserId(userId)
                }
                verify(exactly = 0) { deviceTokenPort.deleteDeviceTokenByUserId(userId) }
            }
        }
    }

    describe("deleteNotificationOfUserByUserIdAndId") {
        context("사용자의 알림을 삭제하면") {
            val userId = UUID.randomUUID()
            val notification = createNotificationOfUserStub(userId = userId)

            every { queryNotificationOfUserPort.queryNotificationOfUserById(notification.id) } returns notification
            every { commandNotificationOfUserPort.deleteNotificationOfUserById(notification.id) } just runs

            service.deleteNotificationOfUserByUserIdAndId(userId, notification.id)

            it("알림을 삭제한다") {
                verify(exactly = 1) { commandNotificationOfUserPort.deleteNotificationOfUserById(notification.id) }
            }
        }

        context("다른 사용자의 알림을 삭제하려고 하면") {
            val userId = UUID.randomUUID()
            val notification = createNotificationOfUserStub(userId = UUID.randomUUID())

            every { queryNotificationOfUserPort.queryNotificationOfUserById(notification.id) } returns notification

            it("NotificationOfUserNotFoundException을 던지고 삭제하지 않는다") {
                shouldThrow<NotificationOfUserNotFoundException> {
                    service.deleteNotificationOfUserByUserIdAndId(userId, notification.id)
                }
                verify(exactly = 0) { commandNotificationOfUserPort.deleteNotificationOfUserById(notification.id) }
            }
        }

        context("존재하지 않는 알림을 삭제하려고 하면") {
            val userId = UUID.randomUUID()
            val notificationId = UUID.randomUUID()

            every { queryNotificationOfUserPort.queryNotificationOfUserById(notificationId) } returns null

            it("NotificationOfUserNotFoundException을 던진다") {
                shouldThrow<NotificationOfUserNotFoundException> {
                    service.deleteNotificationOfUserByUserIdAndId(userId, notificationId)
                }
            }
        }
    }

    describe("deleteNotificationOfUserByUserId") {
        context("사용자의 모든 알림을 삭제하면") {
            val userId = UUID.randomUUID()

            every { commandNotificationOfUserPort.deleteNotificationOfUserByUserId(userId) } just runs

            service.deleteNotificationOfUserByUserId(userId)

            it("모든 알림을 삭제한다") {
                verify(exactly = 1) { commandNotificationOfUserPort.deleteNotificationOfUserByUserId(userId) }
            }
        }
    }

    describe("saveNotificationOfUser") {
        context("알림을 저장하면") {
            val notification = createNotificationOfUserStub()

            every { commandNotificationOfUserPort.saveNotificationOfUser(notification) } returns notification

            val result = service.saveNotificationOfUser(notification)

            it("저장된 알림을 반환한다") {
                result shouldBe notification
            }
        }
    }

    describe("saveNotificationsOfUser") {
        context("여러 알림을 저장하면") {
            val notifications = listOf(createNotificationOfUserStub(), createNotificationOfUserStub())

            every { commandNotificationOfUserPort.saveNotificationsOfUser(notifications) } just runs

            service.saveNotificationsOfUser(notifications)

            it("모든 알림을 저장한다") {
                verify(exactly = 1) { commandNotificationOfUserPort.saveNotificationsOfUser(notifications) }
            }
        }
    }

    describe("deleteOldNotifications") {
        context("오래된 알림을 삭제하면") {
            every { commandNotificationOfUserPort.deleteOldNotificationOfUsers(any()) } just runs

            service.deleteOldNotifications()

            it("60일 이전의 알림을 삭제한다") {
                verify(exactly = 1) {
                    commandNotificationOfUserPort.deleteOldNotificationOfUsers(
                        match { cutoffDate ->
                            val expectedCutoff = LocalDateTime.now().minusDays(60)
                            cutoffDate.isBefore(expectedCutoff.plusMinutes(1)) &&
                                cutoffDate.isAfter(expectedCutoff.minusMinutes(1))
                        }
                    )
                }
            }
        }
    }

    describe("subscribeTopic") {
        context("토큰과 주제로 구독하면") {
            val deviceToken = createDeviceTokenStub(token = "subscribe-token")
            val topic = Topic.NOTICE

            every { deviceTokenPort.queryDeviceTokenByToken(deviceToken.token) } returns deviceToken
            every { notificationPort.subscribeTopic(deviceToken.token, topic) } just runs

            service.subscribeTopic(deviceToken.token, topic)

            it("구독 상태를 저장한다") {
                verify(exactly = 1) {
                    topicSubscriptionPort.saveTopicSubscription(TopicSubscription.subscribe(deviceToken.id, topic))
                }
            }

            it("FCM 주제를 구독한다") {
                verify(exactly = 1) { notificationPort.subscribeTopic(deviceToken.token, topic) }
            }
        }

        context("등록되지 않은 토큰으로 구독하면") {
            val token = "unknown-subscribe-token"

            every { deviceTokenPort.queryDeviceTokenByToken(token) } returns null

            it("DeviceTokenNotFoundException을 던지고 구독하지 않는다") {
                shouldThrow<DeviceTokenNotFoundException> {
                    service.subscribeTopic(token, Topic.NOTICE)
                }
                verify(exactly = 0) { notificationPort.subscribeTopic(token, any()) }
            }
        }
    }

    describe("unsubscribeTopic") {
        context("토큰과 주제로 구독 해제하면") {
            val deviceToken = createDeviceTokenStub(token = "unsubscribe-token")
            val topic = Topic.NOTICE

            every { deviceTokenPort.queryDeviceTokenByToken(deviceToken.token) } returns deviceToken
            every { notificationPort.unsubscribeTopic(deviceToken.token, topic) } just runs

            service.unsubscribeTopic(deviceToken.token, topic)

            it("구독 해제 상태를 저장한다") {
                verify(exactly = 1) {
                    topicSubscriptionPort.saveTopicSubscription(TopicSubscription.unsubscribe(deviceToken.id, topic))
                }
            }

            it("FCM 주제 구독을 해제한다") {
                verify(exactly = 1) { notificationPort.unsubscribeTopic(deviceToken.token, topic) }
            }
        }
    }

    describe("updateSubscribes") {
        context("주제별 구독 여부를 한꺼번에 바꾸면") {
            val deviceToken = createDeviceTokenStub(token = "update-token")
            val saved = slot<List<TopicSubscription>>()

            every { deviceTokenPort.queryDeviceTokenByToken(deviceToken.token) } returns deviceToken
            every { notificationPort.subscribeTopic(deviceToken.token, Topic.NOTICE) } just runs
            every { notificationPort.unsubscribeTopic(deviceToken.token, Topic.POINT) } just runs
            every { topicSubscriptionPort.saveAllTopicSubscriptions(capture(saved)) } just runs

            service.updateSubscribes(
                deviceToken.token,
                listOf(Topic.NOTICE to true, Topic.POINT to false)
            )

            it("true인 주제는 구독하고 false인 주제는 구독 해제한다") {
                verify(exactly = 1) { notificationPort.subscribeTopic(deviceToken.token, Topic.NOTICE) }
                verify(exactly = 1) { notificationPort.unsubscribeTopic(deviceToken.token, Topic.POINT) }
            }

            it("주제별 구독 상태를 한 번에 저장한다") {
                saved.captured shouldContainExactly listOf(
                    TopicSubscription(deviceTokenId = deviceToken.id, topic = Topic.NOTICE, isSubscribed = true),
                    TopicSubscription(deviceTokenId = deviceToken.id, topic = Topic.POINT, isSubscribed = false)
                )
            }
        }
    }

    describe("toggleSubscription") {
        context("구독 중인 주제를 토글하면") {
            val deviceToken = createDeviceTokenStub(token = "toggle-off-token")
            val topic = Topic.NOTICE

            every { deviceTokenPort.queryDeviceTokenByToken(deviceToken.token) } returns deviceToken
            every { topicSubscriptionPort.queryDeviceTokenIdAndTopic(deviceToken.id, topic) } returns
                createTopicSubscriptionStub(deviceTokenId = deviceToken.id, topic = topic, isSubscribed = true)
            every { notificationPort.unsubscribeTopic(deviceToken.token, topic) } just runs

            service.toggleSubscription(deviceToken.token, topic)

            it("FCM 주제 구독을 해제하고 해제 상태로 저장한다") {
                verify(exactly = 1) { notificationPort.unsubscribeTopic(deviceToken.token, topic) }
                verify(exactly = 0) { notificationPort.subscribeTopic(deviceToken.token, topic) }
                verify(exactly = 1) {
                    topicSubscriptionPort.saveTopicSubscription(
                        TopicSubscription(deviceTokenId = deviceToken.id, topic = topic, isSubscribed = false)
                    )
                }
            }
        }

        context("구독 해제된 주제를 토글하면") {
            val deviceToken = createDeviceTokenStub(token = "toggle-on-token")
            val topic = Topic.NOTICE

            every { deviceTokenPort.queryDeviceTokenByToken(deviceToken.token) } returns deviceToken
            every { topicSubscriptionPort.queryDeviceTokenIdAndTopic(deviceToken.id, topic) } returns
                createTopicSubscriptionStub(deviceTokenId = deviceToken.id, topic = topic, isSubscribed = false)
            every { notificationPort.subscribeTopic(deviceToken.token, topic) } just runs

            service.toggleSubscription(deviceToken.token, topic)

            it("FCM 주제를 구독하고 구독 상태로 저장한다") {
                verify(exactly = 1) { notificationPort.subscribeTopic(deviceToken.token, topic) }
                verify(exactly = 0) { notificationPort.unsubscribeTopic(deviceToken.token, topic) }
                verify(exactly = 1) {
                    topicSubscriptionPort.saveTopicSubscription(
                        TopicSubscription(deviceTokenId = deviceToken.id, topic = topic, isSubscribed = true)
                    )
                }
            }
        }

        context("구독 기록이 없는 주제를 토글하면") {
            val deviceToken = createDeviceTokenStub(token = "toggle-none-token")
            val topic = Topic.NOTICE

            every { deviceTokenPort.queryDeviceTokenByToken(deviceToken.token) } returns deviceToken
            every { topicSubscriptionPort.queryDeviceTokenIdAndTopic(deviceToken.id, topic) } returns null

            service.toggleSubscription(deviceToken.token, topic)

            it("아무것도 바꾸지 않는다") {
                verify(exactly = 0) { notificationPort.subscribeTopic(deviceToken.token, topic) }
                verify(exactly = 0) { notificationPort.unsubscribeTopic(deviceToken.token, topic) }
                verify(exactly = 0) {
                    topicSubscriptionPort.saveTopicSubscription(match { it.deviceTokenId == deviceToken.id })
                }
            }
        }
    }

    describe("sendMessage") {
        context("저장이 필요한 알림을 전송하면") {
            val deviceToken = createDeviceTokenStub(token = "send-save-token")
            val notification = createNotificationStub(isSaveRequired = true)
            val saved = slot<NotificationOfUser>()

            every { commandNotificationOfUserPort.saveNotificationOfUser(capture(saved)) } answers { firstArg() }
            every { notificationPort.sendMessage(deviceToken.token, notification) } just runs

            service.sendMessage(deviceToken, notification)

            it("알림함에 저장한다") {
                saved.captured.userId shouldBe deviceToken.userId
                saved.captured.title shouldBe notification.title
            }

            it("알림을 전송한다") {
                verify(exactly = 1) { notificationPort.sendMessage(deviceToken.token, notification) }
            }
        }

        context("저장이 필요하지 않은 알림을 전송하면") {
            val deviceToken = createDeviceTokenStub(token = "send-no-save-token")
            val notification = createNotificationStub(isSaveRequired = false)

            every { notificationPort.sendMessage(deviceToken.token, notification) } just runs

            it("알림함에 저장하지 않고 알림을 전송한다") {
                clearMocks(notificationPort, commandNotificationOfUserPort, answers = false)

                service.sendMessage(deviceToken, notification)

                verify(exactly = 1) { notificationPort.sendMessage(deviceToken.token, notification) }
                verify(exactly = 0) { commandNotificationOfUserPort.saveNotificationOfUser(any()) }
            }
        }
    }

    describe("sendMessages") {
        context("여러 디바이스에 저장이 필요한 알림을 전송하면") {
            val deviceTokens = listOf(
                createDeviceTokenStub(token = "sends-save-token1"),
                createDeviceTokenStub(token = "sends-save-token2")
            )
            val notification = createNotificationStub(isSaveRequired = true)
            val saved = slot<List<NotificationOfUser>>()

            every { commandNotificationOfUserPort.saveNotificationsOfUser(capture(saved)) } just runs
            every { notificationPort.sendMessages(deviceTokens.map { it.token }, notification) } just runs

            service.sendMessages(deviceTokens, notification)

            it("사용자마다 알림함에 저장한다") {
                saved.captured.map { it.userId } shouldContainExactlyInAnyOrder deviceTokens.map { it.userId }
            }

            it("모든 토큰으로 알림을 전송한다") {
                verify(exactly = 1) { notificationPort.sendMessages(deviceTokens.map { it.token }, notification) }
            }
        }

        context("여러 디바이스에 저장이 필요하지 않은 알림을 전송하면") {
            val deviceTokens = listOf(
                createDeviceTokenStub(token = "sends-no-save-token1"),
                createDeviceTokenStub(token = "sends-no-save-token2")
            )
            val notification = createNotificationStub(topic = Topic.DAYBREAK_STUDY_APPLICATION, isSaveRequired = false)

            every { notificationPort.sendMessages(deviceTokens.map { it.token }, notification) } just runs

            it("알림함에 저장하지 않고 여러 알림을 전송한다") {
                clearMocks(notificationPort, commandNotificationOfUserPort, answers = false)

                service.sendMessages(deviceTokens, notification)

                verify(exactly = 1) { notificationPort.sendMessages(deviceTokens.map { it.token }, notification) }
                verify(exactly = 0) { commandNotificationOfUserPort.saveNotificationsOfUser(any()) }
            }
        }

        context("디바이스 토큰이 하나도 없으면") {
            val notification = createNotificationStub(isSaveRequired = true)

            it("알림 전송을 시도하지 않는다") {
                clearMocks(notificationPort, commandNotificationOfUserPort, answers = false)

                service.sendMessages(emptyList(), notification)

                verify(exactly = 0) { notificationPort.sendMessages(any(), any()) }
                verify(exactly = 0) { commandNotificationOfUserPort.saveNotificationsOfUser(any()) }
            }
        }
    }

    describe("sendMessagesByTopic") {
        context("저장이 필요한 알림을 주제로 전송하면") {
            val notification = createNotificationStub(topic = Topic.NOTICE, isSaveRequired = true)
            val subscribers = listOf(createDeviceTokenStub(), createDeviceTokenStub())
            val saved = slot<List<NotificationOfUser>>()

            every {
                deviceTokenPort.queryDeviceTokensBySubscriptionTopicAndSchoolId(notification.topic, notification.schoolId)
            } returns subscribers
            every { commandNotificationOfUserPort.saveNotificationsOfUser(capture(saved)) } just runs
            every { notificationPort.sendByTopic(notification) } just runs

            service.sendMessagesByTopic(notification)

            it("그 학교에서 주제를 구독한 사용자마다 알림함에 저장한다") {
                saved.captured.map { it.userId } shouldContainExactlyInAnyOrder subscribers.map { it.userId }
            }

            it("주제로 알림을 전송한다") {
                verify(exactly = 1) { notificationPort.sendByTopic(notification) }
            }
        }

        context("저장이 필요하지 않은 알림을 주제로 전송하면") {
            val notification = createNotificationStub(topic = Topic.NOTICE, isSaveRequired = false)

            every { notificationPort.sendByTopic(notification) } just runs

            it("구독자를 조회하거나 알림함에 저장하지 않고 전송만 한다") {
                clearMocks(notificationPort, commandNotificationOfUserPort, deviceTokenPort, answers = false)

                service.sendMessagesByTopic(notification)

                verify(exactly = 1) { notificationPort.sendByTopic(notification) }
                verify(exactly = 0) { deviceTokenPort.queryDeviceTokensBySubscriptionTopicAndSchoolId(any(), any()) }
                verify(exactly = 0) { commandNotificationOfUserPort.saveNotificationsOfUser(any()) }
            }
        }
    }
})
