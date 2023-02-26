package team.aliens.dms.domain.student.model

import team.aliens.dms.common.annotation.Aggregate
import java.util.UUID

@Aggregate
data class VerifiedStudent(

    val id: UUID,

    val schoolName: String,

    val name: String,

    val roomNumber: Int,

    val gcn: String,

    val sex: Sex

)
