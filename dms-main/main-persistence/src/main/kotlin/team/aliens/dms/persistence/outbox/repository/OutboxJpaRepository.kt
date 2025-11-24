package team.aliens.dms.persistence.outbox.repository

import org.springframework.data.jpa.repository.JpaRepository
import team.aliens.dms.persistence.outbox.entity.OutboxJpaEntity
import team.aliens.dms.persistence.outbox.entity.OutboxStatus
import java.util.UUID

interface OutboxJpaRepository : JpaRepository<OutboxJpaEntity, UUID> {
    fun findByStatus(status: OutboxStatus): List<OutboxJpaEntity>
    fun findByStatusAndPayload(status: OutboxStatus, payload: String): OutboxJpaEntity?
}
