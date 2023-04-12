package team.aliens.dms.domain.point.model

import team.aliens.dms.common.annotation.Aggregate
import team.aliens.dms.domain.point.exception.PointOptionSchoolMismatchException
import java.util.UUID

@Aggregate
data class PointOption(

    val id: UUID = UUID(0, 0),

    val schoolId: UUID,

    val name: String,

    val score: Int,

    val type: PointType

) {
    fun checkSchoolId(schoolId: UUID) {
        if (schoolId != this.schoolId) {
            throw PointOptionSchoolMismatchException
        }
    }
}
