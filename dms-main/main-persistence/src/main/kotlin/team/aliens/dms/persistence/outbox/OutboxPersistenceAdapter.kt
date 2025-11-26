package team.aliens.dms.persistence.outbox

import org.springframework.stereotype.Component
import team.aliens.dms.common.dto.OutboxData
import team.aliens.dms.common.dto.OutboxStatus
import team.aliens.dms.common.spi.OutboxPort
import team.aliens.dms.persistence.outbox.mapper.OutboxMapper
import team.aliens.dms.persistence.outbox.repository.OutboxJpaRepository
import java.util.UUID

@Component
class OutboxPersistenceAdapter(
    private val outboxJpaRepository: OutboxJpaRepository,
    private val outboxMapper: OutboxMapper
) : OutboxPort {

    override fun save(outbox: OutboxData): OutboxData {
        val entity = outboxMapper.toEntity(outbox)
        val saved = outboxJpaRepository.save(entity)
        return outboxMapper.toDomain(saved)
    }

    override fun deleteById(id: UUID) {
        outboxJpaRepository.deleteById(id)
    }

    override fun findByStatus(status: OutboxStatus): List<OutboxData> {
        val jpaStatus = outboxMapper.toJpaStatus(status)
        return outboxJpaRepository.findByStatus(jpaStatus)
            .map { outboxMapper.toDomain(it) }
    }

    override fun delete(outbox: OutboxData) {
        val entity = outboxMapper.toEntity(outbox)
        outboxJpaRepository.delete(entity)
    }
}
