package team.aliens.dms.common.spi

import team.aliens.dms.common.dto.OutboxData
import team.aliens.dms.common.dto.OutboxStatus
import java.util.UUID

interface OutboxPort {
    fun save(outbox: OutboxData): OutboxData
    fun deleteById(id: UUID)
    fun findByStatus(status: OutboxStatus): List<OutboxData>
    fun delete(outbox: OutboxData)
}
