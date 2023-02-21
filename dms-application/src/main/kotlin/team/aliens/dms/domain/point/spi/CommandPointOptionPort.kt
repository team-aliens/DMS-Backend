package team.aliens.dms.domain.point.spi

import team.aliens.dms.domain.point.model.PointOption
import java.util.UUID

interface CommandPointOptionPort {
    fun savePointOption(pointOption: PointOption): UUID

    fun deletePointOption(pointOption: PointOption)
}