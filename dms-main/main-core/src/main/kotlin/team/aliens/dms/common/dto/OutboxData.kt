package team.aliens.dms.common.dto

import java.time.LocalDateTime
import java.util.UUID

data class OutboxData(
    val id: UUID?,
    val aggregateType: String,
    val eventType: String,
    val payload: String,
    val status: OutboxStatus,
    val retryCount: Int = 0,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val processedAt: LocalDateTime? = null
)
