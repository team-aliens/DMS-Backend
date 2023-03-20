package team.aliens.dms.domain.remain.stub

import team.aliens.dms.domain.remain.model.RemainStatus
import java.time.LocalDateTime
import java.util.UUID

internal fun createRemainStatusStub(
    id: UUID = UUID.randomUUID(),
    remainOptionId: UUID = UUID.randomUUID(),
    createdAt: LocalDateTime = LocalDateTime.now()
) = RemainStatus(
    id = id,
    remainOptionId = remainOptionId,
    createdAt = createdAt
)
