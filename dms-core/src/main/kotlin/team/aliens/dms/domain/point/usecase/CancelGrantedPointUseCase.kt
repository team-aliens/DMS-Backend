package team.aliens.dms.domain.point.usecase

import java.util.UUID
import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.point.service.PointService

@UseCase
class CancelGrantedPointUseCase(
    private val pointService: PointService
) {

    fun execute(pointHistoryId: UUID) {

        val pointHistory = pointService.getPointHistoryById(pointHistoryId)

        val pointTotal = pointService.queryBonusAndMinusTotalPointByStudentGcnAndName(
            gcn = pointHistory.studentGcn,
            studentName = pointHistory.studentName
        )

        pointService.savePointHistory(pointHistory.cancelHistory(pointTotal))
        pointService.deletePointHistory(pointHistory)
    }
}
