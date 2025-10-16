package team.aliens.dms.domain.point.spi

import team.aliens.dms.domain.point.model.PointOption

interface CommandPointOptionPort {
    fun savePointOption(pointOption: PointOption): PointOption

    fun deletePointOption(pointOption: PointOption)
}
