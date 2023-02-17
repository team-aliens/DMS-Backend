package team.aliens.dms.domain.point.spi

import team.aliens.dms.domain.point.model.PointHistory

interface CommandPointPort {
    fun saveAllPointHistories(pointHistories: List<PointHistory>)
}