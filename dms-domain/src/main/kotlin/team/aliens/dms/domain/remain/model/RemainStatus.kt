package team.aliens.dms.domain.remain.model

import team.aliens.dms.common.annotation.Aggregate
import java.time.LocalDateTime
import java.util.UUID

@Aggregate
data class RemainStatus (

    val id: UUID = UUID(0, 0),

    val remainOptionId: UUID,

    val createdAt: LocalDateTime

)