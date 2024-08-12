package team.aliens.dms.domain.point.service

import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.point.model.PointOption
import java.util.UUID

interface CommandPointService {

    fun savePointHistory(pointHistory: PointHistory): PointHistory

    fun deletePointHistory(pointHistory: PointHistory)

    fun saveAllPointHistories(pointHistories: List<PointHistory>, studentIds: List<UUID>?)

    fun savePointOption(pointOption: PointOption): PointOption

    fun deletePointOption(pointOption: PointOption)
}
