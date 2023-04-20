package team.aliens.dms.domain.point.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.point.service.PointService
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@UseCase
class CancelGrantedPointUseCase(
    private val pointService: PointService,
    private val userService: UserService,
) {

    fun execute(pointHistoryId: UUID) {

        val user = userService.getCurrentUser()

        val pointHistory = pointService.getPointHistoryById(pointHistoryId, user.schoolId)

        val pointTotal = pointService.queryBonusAndMinusTotalPointByStudentGcnAndName(
            gcn = pointHistory.studentGcn,
            studentName = pointHistory.studentName
        )

        pointService.savePointHistory(pointHistory.cancelHistory(pointTotal))
        pointService.deletePointHistory(pointHistory)
    }
}
