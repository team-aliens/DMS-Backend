package team.aliens.dms.persistence.notification

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Component
import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.spi.DeviceTokenPort
import team.aliens.dms.persistence.notification.entity.QDeviceTokenJpaEntity.deviceTokenJpaEntity
import team.aliens.dms.persistence.notification.mapper.DeviceTokenMapper
import team.aliens.dms.persistence.notification.repository.DeviceTokenJpaRepository
import team.aliens.dms.persistence.student.entity.QStudentJpaEntity.studentJpaEntity
import team.aliens.dms.persistence.user.entity.QUserJpaEntity.userJpaEntity
import java.util.UUID

@Component
class DeviceTokenPersistenceAdapter(
    private val notificationMapper: DeviceTokenMapper,
    private val deviceTokenRepository: DeviceTokenJpaRepository,
    private val queryFactory: JPAQueryFactory
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

    override fun queryDeviceTokensByUserIds(studentIds: List<UUID>): List<DeviceToken> {
        return queryFactory
            .selectFrom(deviceTokenJpaEntity)
            .join(userJpaEntity).on(userJpaEntity.id.eq(deviceTokenJpaEntity.user.id))
            .join(studentJpaEntity).on(userJpaEntity.id.eq(studentJpaEntity.user.id))
            .where(studentJpaEntity.id.`in`(studentIds))
            .fetch()
            .map {
                notificationMapper.toDomain(it)!!
            }
    }

    override fun deleteDeviceTokenByUserId(userId: UUID) {
        deviceTokenRepository.deleteByUserId(userId)
    }
}
