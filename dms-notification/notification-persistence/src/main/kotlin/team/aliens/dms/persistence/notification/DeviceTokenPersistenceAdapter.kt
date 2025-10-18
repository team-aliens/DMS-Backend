package team.aliens.dms.persistence.notification

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import team.aliens.dms.contract.model.notification.Topic
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.spi.DeviceTokenPort
import team.aliens.dms.persistence.notification.entity.QDeviceTokenJpaEntity.deviceTokenJpaEntity
import team.aliens.dms.persistence.notification.entity.QTopicSubscriptionJpaEntity.topicSubscriptionJpaEntity
import team.aliens.dms.persistence.notification.mapper.DeviceTokenMapper
import team.aliens.dms.persistence.notification.repository.DeviceTokenJpaRepository
import java.util.UUID

@Component
class DeviceTokenPersistenceAdapter(
    private val notificationMapper: DeviceTokenMapper,
    private val deviceTokenRepository: DeviceTokenJpaRepository,
    private val queryFactory: JPAQueryFactory,
) : DeviceTokenPort {

    override fun existsDeviceTokenByUserId(userId: UUID): Boolean {
        return deviceTokenRepository.existsByUserId(userId)
    }

    override fun saveDeviceToken(deviceToken: DeviceToken) = notificationMapper.toDomain(
        deviceTokenRepository.save(
            notificationMapper.toEntity(deviceToken)
        )
    )!!

    override fun queryDeviceTokenByUserId(userId: UUID) = notificationMapper.toDomain(
        deviceTokenRepository.findByUserId(userId)
    )

    override fun queryDeviceTokenByToken(token: String) = notificationMapper.toDomain(
        deviceTokenRepository.findByToken(token)
    )

    override fun queryDeviceTokensByUserIds(userIds: List<UUID>): List<DeviceToken> =
        queryFactory
            .selectFrom(deviceTokenJpaEntity)
            .where(deviceTokenJpaEntity.userId.`in`(userIds))
            .fetch()
            .map { notificationMapper.toDomain(it)!! }

    override fun queryDeviceTokensBySubscriptionTopicAndSchoolId(
        topic: Topic,
        schoolId: UUID
    ): List<DeviceToken> {
        return queryFactory
            .selectFrom(deviceTokenJpaEntity)
            .join(topicSubscriptionJpaEntity).on(deviceTokenJpaEntity.id.eq(topicSubscriptionJpaEntity.deviceToken.id))
            .where(
                topicSubscriptionJpaEntity.id.topic.eq(topic)
                    .and(deviceTokenJpaEntity.schoolId.eq(schoolId))
            )
            .fetch()
            .map {
                notificationMapper.toDomain(it)!!
            }
    }

    override fun deleteDeviceTokenByUserId(userId: UUID) {
        deviceTokenRepository.deleteByUserId(userId)
    }
}
