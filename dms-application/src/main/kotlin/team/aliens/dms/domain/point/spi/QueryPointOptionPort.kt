package team.aliens.dms.domain.point.spi

import team.aliens.dms.domain.point.model.PointOption
import java.util.*

interface QueryPointOptionPort {
    fun queryPointOptionById(pointOptionId: UUID): PointOption?
}