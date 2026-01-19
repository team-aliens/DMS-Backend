package team.aliens.dms.common.spi

import team.aliens.dms.common.dto.OutboxData
import team.aliens.dms.common.dto.OutboxStatus

interface OutboxPort {
    fun save(outbox: OutboxData): OutboxData
    fun findByStatus(status: OutboxStatus): List<OutboxData>
    fun delete(outbox: OutboxData)
}
