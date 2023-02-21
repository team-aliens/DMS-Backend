package team.aliens.dms.domain.point.spi

import team.aliens.dms.domain.point.model.PointOption
import java.util.UUID

interface QueryPointOptionPort {
    fun queryPointOptionById(pointOptionId: UUID): PointOption?
}