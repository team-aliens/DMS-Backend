package team.aliens.dms.domain.point.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.point.dto.PointRequestType
import team.aliens.dms.domain.point.dto.QueryPointHistoryResponse
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.PointQueryStudentPort
import team.aliens.dms.domain.point.spi.PointSecurityPort
import team.aliens.dms.domain.point.spi.QueryPointPort
import team.aliens.dms.domain.student.exception.StudentNotFoundException

@ReadOnlyUseCase
class QueryPointHistoryUseCase(
    private val securityPort: PointSecurityPort,
    private val queryStudentPort: PointQueryStudentPort,
    private val queryPointPort: QueryPointPort
) {

    fun execute(type: PointRequestType): QueryPointHistoryResponse {

        val currentStudentId = securityPort.getCurrentUserId()
        val currentStudent = queryStudentPort.queryStudentById(currentStudentId) ?: throw StudentNotFoundException

        val pointType = PointRequestType.toPointType(type)
        val pointHistory = if (pointType != null) {
            queryPointPort.queryPointHistoryByStudentAndType(currentStudent, pointType)
        } else {
            queryPointPort.queryAllPointHistoryByStudent(currentStudent)
        }

        val totalPoint = pointHistory.sumOf {
            if (type == PointRequestType.ALL && it.type == PointType.MINUS) {
                it.score * -1
            } else {
                it.score
            }
        }

        return QueryPointHistoryResponse(
            /**
             * BONUS -> 상점의 총합 ex) 상점 3점 = 3
             * MINUS -> 벌점의 총합 ex) 벌점 4점 = 4
             * ALL -> 상점 + 벌점의 총합 상점 3점, 벌점 4점 = -1
             **/
            totalPoint = totalPoint,
            points = pointHistory
        )
    }
}