package team.aliens.dms.domain.point.spi

import team.aliens.dms.domain.point.dto.QueryPointHistoryResponse
import team.aliens.dms.domain.point.model.PointType

interface QueryPointPort {

    fun queryBonusAndMinusTotalPointByGcnAndStudentName(
        gcn: String,
        studentName: String,
    ): Pair<Int, Int>

    fun queryPointHistoryByGcnAndStudentNameAndType(
        gcn: String,
        studentName: String,
        type: PointType,
        isCancel: Boolean? = null
    ): List<QueryPointHistoryResponse.Point>

    fun queryPointHistoryByGcnAndStudentName(
        gcn: String,
        studentName: String,
        isCancel: Boolean? = null
    ): List<QueryPointHistoryResponse.Point>

}
