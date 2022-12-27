package team.aliens.dms.domain.student.model

import team.aliens.dms.common.annotation.Aggregate
import java.util.UUID

@Aggregate
data class VerifiedStudent(

    val id: UUID = UUID(0, 0),

    val schoolName: String,

    val name: String,

    val roomNumber: Int,

    val gcn: String,

    val sex: Sex

)