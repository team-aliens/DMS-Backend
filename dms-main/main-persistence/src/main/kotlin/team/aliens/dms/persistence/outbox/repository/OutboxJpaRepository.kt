package team.aliens.dms.persistence.outbox.repository

import org.springframework.data.repository.CrudRepository
import team.aliens.dms.persistence.outbox.entity.OutboxJpaEntity
import team.aliens.dms.persistence.outbox.entity.OutboxStatus
import java.util.UUID

interface OutboxJpaRepository : CrudRepository<OutboxJpaEntity, UUID> {
    fun findByStatus(status: OutboxStatus): List<OutboxJpaEntity>
}
