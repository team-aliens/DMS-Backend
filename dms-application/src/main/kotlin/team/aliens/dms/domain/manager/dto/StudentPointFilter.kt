package team.aliens.dms.domain.manager.dto

import team.aliens.dms.domain.point.exception.InvalidPointFilterRangeException

data class StudentPointFilter (
    val filterType: PointFilterType,
    val minPoint: Int,
    val maxPoint: Int
) {

    companion object {
        fun init(filterType: PointFilterType?, minPoint: Int?, maxPoint: Int?): StudentPointFilter? {
            return filterType?.let {
                if(maxPoint == null || minPoint == null || maxPoint < minPoint) {
                    throw InvalidPointFilterRangeException
                }

                StudentPointFilter(
                    filterType = filterType,
                    maxPoint = maxPoint,
                    minPoint = minPoint
                )
            }
        }
    }

}