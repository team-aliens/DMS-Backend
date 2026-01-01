package team.aliens.dms.persistence.outbox.mapper

import org.springframework.stereotype.Component
import team.aliens.dms.common.dto.OutboxData
import team.aliens.dms.common.dto.OutboxStatus
import team.aliens.dms.persistence.outbox.entity.OutboxJpaEntity
import team.aliens.dms.persistence.outbox.entity.OutboxStatus as JpaOutboxStatus

@Component
class OutboxMapper {

    fun toDomain(entity: OutboxJpaEntity): OutboxData {
        return OutboxData(
            id = entity.id,
            aggregateType = entity.aggregateType,
            eventType = entity.eventType,
            payload = entity.payload,
            status = toOutboxStatus(entity.status),
            retryCount = entity.retryCount,
            createdAt = entity.createdAt,
            processedAt = entity.processedAt
        )
    }

    fun toEntity(domain: OutboxData): OutboxJpaEntity {
        return OutboxJpaEntity(
            id = domain.id,
            aggregateType = domain.aggregateType,
            eventType = domain.eventType,
            payload = domain.payload,
            status = toJpaStatus(domain.status),
            retryCount = domain.retryCount,
            createdAt = domain.createdAt,
            processedAt = domain.processedAt
        )
    }

    fun toJpaStatus(status: OutboxStatus): JpaOutboxStatus {
        return when (status) {
            OutboxStatus.PENDING -> JpaOutboxStatus.PENDING
            OutboxStatus.PROCESSED -> JpaOutboxStatus.PROCESSED
            OutboxStatus.FAILED -> JpaOutboxStatus.FAILED
        }
    }

    private fun toOutboxStatus(status: JpaOutboxStatus): OutboxStatus {
        return when (status) {
            JpaOutboxStatus.PENDING -> OutboxStatus.PENDING
            JpaOutboxStatus.PROCESSED -> OutboxStatus.PROCESSED
            JpaOutboxStatus.FAILED -> OutboxStatus.FAILED
        }
    }
}
