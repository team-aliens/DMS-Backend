package team.aliens.dms.domain.manager.dto

import team.aliens.dms.domain.point.exception.InvalidPointFilterRangeException

data class PointFilter (
    val filterType: PointFilterType?,
    val minPoint: Int?,
    val maxPoint: Int?
) {
    init {
        filterType?.let {
            if(maxPoint == null || minPoint == null || maxPoint < minPoint) {
                throw InvalidPointFilterRangeException
            }
        }
    }
}
