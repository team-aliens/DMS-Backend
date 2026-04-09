package team.aliens.dms.domain.teacher.model

import team.aliens.dms.common.annotation.Aggregate
import java.util.UUID

@Aggregate
data class Teacher(
    val id: UUID,
    val name: String,
    val role: Role
)
