package team.aliens.dms.domain.point.model

import team.aliens.dms.common.annotation.Aggregate
import team.aliens.dms.common.model.SchoolIdDomain
import java.time.LocalDateTime
import java.util.UUID

@Aggregate
data class PointOption(

    val id: UUID = UUID(0, 0),

    override val schoolId: UUID,

    val name: String,

    val score: Int,

    val type: PointType,

    val createdAt: LocalDateTime?,

) : SchoolIdDomain {

    fun getTitle(): String =
        when (type) {
            PointType.BONUS -> "상점이 부과되었습니다."
            PointType.MINUS -> "벌점이 부과되었습니다."
        }
}
