package team.aliens.dms.domain.point.spi

import team.aliens.dms.domain.point.dto.QueryPointHistoryResponse
import team.aliens.dms.domain.point.model.PointType
import java.util.UUID

interface QueryPointPort {

    fun queryPointHistoryByStudentIdAndType(studentId: UUID, type: PointType): List<QueryPointHistoryResponse.Point>

    fun queryAllPointHistoryByStudentId(studentId: UUID): List<QueryPointHistoryResponse.Point>

}
