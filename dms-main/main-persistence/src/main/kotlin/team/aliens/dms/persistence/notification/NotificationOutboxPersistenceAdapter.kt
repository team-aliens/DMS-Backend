package team.aliens.dms.persistence.notification

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.LockModeType
import org.springframework.stereotype.Component
import team.aliens.dms.domain.notification.model.NotificationOutbox
import team.aliens.dms.domain.notification.model.NotificationOutboxStatus
import team.aliens.dms.domain.notification.spi.NotificationOutboxPort
import team.aliens.dms.persistence.notification.entity.QNotificationOutboxJpaEntity.notificationOutboxJpaEntity
import team.aliens.dms.persistence.notification.mapper.NotificationOutboxMapper
import team.aliens.dms.persistence.notification.repository.NotificationOutboxJpaRepository
import java.util.UUID

@Component
class NotificationOutboxPersistenceAdapter(
    private val notificationOutboxMapper: NotificationOutboxMapper,
    private val notificationOutboxRepository: NotificationOutboxJpaRepository,
    private val queryFactory: JPAQueryFactory
) : NotificationOutboxPort {

    override fun saveNotificationOutbox(notificationOutbox: NotificationOutbox) =
        notificationOutboxMapper.toDomain(
            notificationOutboxRepository.save(
                notificationOutboxMapper.toEntity(notificationOutbox)
            )
        )!!

    override fun deleteNotificationOutboxById(id: UUID) {
        notificationOutboxRepository.deleteById(id)
    }

    // 커밋 직후 비동기 발송과 스케줄러, 무중단 배포 중 겹친 두 인스턴스가 같은 행을 동시에 보내지 않도록 잠근다.
    // 나중에 온 쪽은 앞의 발송이 끝날 때까지 기다린다. 행이 지워졌으면 null, 실패한 유저만 남았으면 그 행을 받는다
    override fun queryPendingNotificationOutboxByIdForUpdate(id: UUID) =
        notificationOutboxMapper.toDomain(
            queryFactory
                .selectFrom(notificationOutboxJpaEntity)
                .where(
                    notificationOutboxJpaEntity.id.eq(id),
                    notificationOutboxJpaEntity.status.eq(NotificationOutboxStatus.PENDING)
                )
                .setLockMode(LockModeType.PESSIMISTIC_WRITE)
                .fetchOne()
        )

    // 재시도 횟수가 적은 행부터 집는다. 계속 실패하는 오래된 행이 새 행을 밀어내지 않게
    override fun queryPendingNotificationOutboxIds(limit: Int): List<UUID> =
        queryFactory
            .select(notificationOutboxJpaEntity.id)
            .from(notificationOutboxJpaEntity)
            .where(notificationOutboxJpaEntity.status.eq(NotificationOutboxStatus.PENDING))
            .orderBy(notificationOutboxJpaEntity.retryCount.asc(), notificationOutboxJpaEntity.createdAt.asc())
            .limit(limit.toLong())
            .fetch()
}
