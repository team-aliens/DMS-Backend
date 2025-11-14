package team.aliens.dms.persistence.notification

import com.querydsl.jpa.impl.JPAQueryFactory
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import team.aliens.dms.contract.model.notification.Topic
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.persistence.notification.entity.DeviceTokenJpaEntity
import team.aliens.dms.persistence.notification.mapper.DeviceTokenMapper
import team.aliens.dms.persistence.notification.repository.DeviceTokenJpaRepository
import java.util.UUID

class DeviceTokenPersistenceAdapterTest : DescribeSpec({

    val mapper = mockk<DeviceTokenMapper>()
    val repository = mockk<DeviceTokenJpaRepository>()
    val queryFactory = mockk<JPAQueryFactory>()

    val adapter = DeviceTokenPersistenceAdapter(
        notificationMapper = mapper,
        deviceTokenRepository = repository,
        queryFactory = queryFactory
    )

    describe("existsDeviceTokenByUserId") {
        context("사용자 ID로 디바이스 토큰 존재 여부를 확인하고 존재하면") {
            val userId = UUID.randomUUID()

            every { repository.existsByUserId(userId) } returns true

            it("true를 반환한다") {
                val result = adapter.existsDeviceTokenByUserId(userId)

                result shouldBe true
                verify(exactly = 1) { repository.existsByUserId(userId) }
            }
        }
    }

    describe("saveDeviceToken") {
        context("디바이스 토큰을 저장하면") {
            val deviceToken = DeviceToken(
                id = UUID.randomUUID(),
                userId = UUID.randomUUID(),
                schoolId = UUID.randomUUID(),
                token = "test-token"
            )
            val entity = mockk<DeviceTokenJpaEntity>()

            every { mapper.toEntity(deviceToken) } returns entity
            every { repository.save(entity) } returns entity
            every { mapper.toDomain(entity) } returns deviceToken

            it("저장된 DeviceToken을 반환한다") {
                val result = adapter.saveDeviceToken(deviceToken)

                result shouldBe deviceToken
                verify(exactly = 1) { mapper.toEntity(deviceToken) }
                verify(exactly = 1) { repository.save(entity) }
                verify(exactly = 1) { mapper.toDomain(entity) }
            }
        }
    }

    describe("queryDeviceTokenByUserId") {
        context("사용자 ID로 디바이스 토큰을 조회하고 존재하면") {
            val userId = UUID.randomUUID()
            val deviceToken = DeviceToken(
                id = UUID.randomUUID(),
                userId = userId,
                schoolId = UUID.randomUUID(),
                token = "test-token"
            )
            val entity = mockk<DeviceTokenJpaEntity>()

            every { repository.findByUserId(userId) } returns entity
            every { mapper.toDomain(entity) } returns deviceToken

            it("DeviceToken을 반환한다") {
                val result = adapter.queryDeviceTokenByUserId(userId)

                result shouldBe deviceToken
                verify(exactly = 1) { repository.findByUserId(userId) }
            }
        }
    }

    describe("queryDeviceTokenByToken") {
        context("토큰으로 디바이스 토큰을 조회하고 존재하면") {
            val token = "test-token"
            val deviceToken = DeviceToken(
                id = UUID.randomUUID(),
                userId = UUID.randomUUID(),
                schoolId = UUID.randomUUID(),
                token = token
            )
            val entity = mockk<DeviceTokenJpaEntity>()

            every { repository.findByToken(token) } returns entity
            every { mapper.toDomain(entity) } returns deviceToken

            it("DeviceToken을 반환한다") {
                val result = adapter.queryDeviceTokenByToken(token)

                result shouldBe deviceToken
                verify(exactly = 1) { repository.findByToken(token) }
            }
        }
    }

    describe("deleteDeviceTokenByUserId") {
        context("사용자 ID로 디바이스 토큰을 삭제하면") {
            val userId = UUID.randomUUID()

            every { repository.deleteByUserId(userId) } returns Unit

            it("디바이스 토큰을 삭제한다") {
                adapter.deleteDeviceTokenByUserId(userId)

                verify(exactly = 1) { repository.deleteByUserId(userId) }
            }
        }
    }
})
