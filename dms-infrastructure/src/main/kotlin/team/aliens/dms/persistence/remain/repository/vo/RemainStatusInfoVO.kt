package team.aliens.dms.persistence.remain.repository.vo

import com.querydsl.core.annotations.QueryProjection
import java.util.UUID

data class RemainStatusInfoVO @QueryProjection constructor(
    val studentId: UUID,
    val optionName: String
)
