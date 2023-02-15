package team.aliens.dms.domain.point.spi

import team.aliens.dms.domain.point.dto.QueryPointHistoryResponse
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.student.model.Student

interface QueryPointPort {

    fun queryPointHistoryByStudentAndType(student: Student, type: PointType): List<QueryPointHistoryResponse.Point>

    fun queryAllPointHistoryByStudent(student: Student): List<QueryPointHistoryResponse.Point>

}
