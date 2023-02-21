package team.aliens.dms.domain.point.spi

import team.aliens.dms.domain.point.model.PointHistory

interface CommandPointHistoryPort {

    fun savePointHistory(pointHistory: PointHistory): PointHistory

    fun deletePointHistory(pointHistory: PointHistory)
}