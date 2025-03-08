package team.aliens.dms.domain.vote.model

import team.aliens.dms.common.annotation.Aggregate
import java.util.UUID

@Aggregate
data class ExcludedStudent(

    val studentId: UUID,

    val schoolId: UUID

)
