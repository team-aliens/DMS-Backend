package team.aliens.dms.persistence.notification.mapper

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.persistence.notification.entity.DeviceTokenJpaEntity
import java.util.UUID

class DeviceTokenMapperTest : DescribeSpec({

    val mapper = DeviceTokenMapper()

    describe("toDomain") {
        context("Entity를 Domain으로 변환하면") {
            val id = UUID.randomUUID()
            val userId = UUID.randomUUID()
            val schoolId = UUID.randomUUID()
            val token = "test-token"

            val entity = DeviceTokenJpaEntity(
                id = id,
                userId = userId,
                schoolId = schoolId,
                token = token
            )

            it("DeviceToken을 반환한다") {
                val domain = mapper.toDomain(entity)

                domain?.id shouldBe id
                domain?.userId shouldBe userId
                domain?.schoolId shouldBe schoolId
                domain?.token shouldBe token
            }
        }

        context("null Entity를 변환하면") {
            it("null을 반환한다") {
                val domain = mapper.toDomain(null)

                domain shouldBe null
            }
        }
    }

    describe("toEntity") {
        context("Domain을 Entity로 변환하면") {
            val id = UUID.randomUUID()
            val userId = UUID.randomUUID()
            val schoolId = UUID.randomUUID()
            val token = "test-token"

            val domain = DeviceToken(
                id = id,
                userId = userId,
                schoolId = schoolId,
                token = token
            )

            it("DeviceTokenJpaEntity를 반환한다") {
                val entity = mapper.toEntity(domain)

                entity.id shouldBe id
                entity.userId shouldBe userId
                entity.schoolId shouldBe schoolId
                entity.token shouldBe token
            }
        }
    }
})
