package team.aliens.dms.domain.student.model

import team.aliens.dms.common.annotation.Aggregate
import java.util.UUID

@Aggregate
data class VerifiedStudent(

    val gcn: String,

    val schoolId: UUID,

    val name: String,

    val roomNumber: Int

)