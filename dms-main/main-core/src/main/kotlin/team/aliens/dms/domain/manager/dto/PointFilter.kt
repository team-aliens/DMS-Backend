package team.aliens.dms.domain.manager.dto

import team.aliens.dms.domain.point.exception.InvalidPointFilterRangeException

data class PointFilter(
    val filterType: PointFilterType?,
    val minPoint: Int?,
    val maxPoint: Int?
) {
    init {
        filterType?.let {
            if (maxPoint == null || minPoint == null || maxPoint < minPoint) {
                throw InvalidPointFilterRangeException
            }
        }
    }

    fun matches(bonusPoint: Int, minusPoint: Int): Boolean {
        val type = filterType ?: return true
        val min = minPoint ?: return true
        val max = maxPoint ?: return true

        val totalPoint = when (type) {
            PointFilterType.BONUS -> bonusPoint
            PointFilterType.MINUS -> minusPoint
            PointFilterType.ALL -> bonusPoint - minusPoint
        }

        return totalPoint in min..max
    }
}
