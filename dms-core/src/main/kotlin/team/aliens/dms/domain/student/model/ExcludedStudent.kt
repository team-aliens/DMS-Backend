package team.aliens.dms.domain.student.model

import team.aliens.dms.common.annotation.Aggregate
import java.util.UUID

@Aggregate
data class ExcludedStudent(

    val studentId: UUID,
) {
    
}