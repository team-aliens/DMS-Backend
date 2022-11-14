package team.aliens.dms.domain.point.spi

import team.aliens.dms.domain.point.dto.vo.QueryPointHistoryVO
import team.aliens.dms.domain.point.model.PointType
import java.util.UUID

interface QueryPointPort {

    fun queryPointHistoryByStudentIdAndType(studentId: UUID, type: PointType): List<QueryPointHistoryVO>

    fun queryAllPointHistoryByStudentId(studentId: UUID): List<QueryPointHistoryVO>

}
