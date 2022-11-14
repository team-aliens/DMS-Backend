package team.aliens.dms.domain.point.dto

import team.aliens.dms.domain.point.model.PointType

enum class PointRequestType {
    ALL, BONUS, MINUS
    ;

    companion object {
        fun toPointType(type: PointRequestType) = when (type) {
            BONUS -> PointType.BONUS
            MINUS -> PointType.MINUS
            else -> null
        }
    }
}
