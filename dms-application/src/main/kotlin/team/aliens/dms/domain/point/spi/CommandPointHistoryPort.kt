package team.aliens.dms.domain.point.spi

import team.aliens.dms.domain.point.model.PointHistory

interface CommandPointHistoryPort {
    fun saveAllPointHistories(pointHistories: List<PointHistory>)
}