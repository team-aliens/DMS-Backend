package team.aliens.dms.domain.point.spi

import team.aliens.dms.domain.point.model.PointOption
import java.util.*

interface QueryPointOptionPort {
    fun queryPointOptionByIdAndSchoolId(
        pointOptionId: UUID,
        schoolId: UUID
    ): PointOption?
}