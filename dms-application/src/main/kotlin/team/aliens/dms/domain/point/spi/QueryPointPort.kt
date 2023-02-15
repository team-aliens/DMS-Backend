package team.aliens.dms.domain.point.spi

import team.aliens.dms.domain.point.dto.QueryPointHistoryResponse
import team.aliens.dms.domain.point.model.PointHistory
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.student.model.Student
import java.util.UUID

interface QueryPointPort {

    fun queryBonusAndMinusTotalPointByStudent(student: Student): Pair<Int, Int>

    fun queryGrantedPointHistoryByStudentAndType(student: Student, type: PointType): List<QueryPointHistoryResponse.Point>

    fun queryGrantedPointHistoryByStudent(student: Student): List<QueryPointHistoryResponse.Point>

}
