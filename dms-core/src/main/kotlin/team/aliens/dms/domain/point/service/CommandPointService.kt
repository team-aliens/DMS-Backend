package team.aliens.dms.domain.point.service

import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.point.model.PointOption

interface CommandPointService {

    fun savePointHistory(pointHistory: PointHistory): PointHistory

    fun deletePointHistory(pointHistory: PointHistory)

    fun saveAllPointHistories(pointHistories: List<PointHistory>)

    fun savePointOption(pointOption: PointOption): PointOption

    fun deletePointOption(pointOption: PointOption)
}