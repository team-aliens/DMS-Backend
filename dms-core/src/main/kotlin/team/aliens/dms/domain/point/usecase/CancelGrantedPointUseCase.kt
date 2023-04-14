package team.aliens.dms.domain.point.usecase

import java.util.UUID
import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.point.exception.PointHistoryNotFoundException
import team.aliens.dms.domain.point.spi.CommandPointHistoryPort
import team.aliens.dms.domain.point.spi.QueryPointHistoryPort
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.user.service.GetUserService

@UseCase
class CancelGrantedPointUseCase(
    private val commandPointHistoryPort: CommandPointHistoryPort,
    private val queryPointHistoryPort: QueryPointHistoryPort,
    private val getUserService: GetUserService,
) {

    fun execute(pointHistoryId: UUID) {

        val user = getUserService.getCurrentUser()

        val pointHistory = queryPointHistoryPort.queryPointHistoryById(pointHistoryId)
            ?: throw PointHistoryNotFoundException

        validateSameSchool(user.schoolId, pointHistory.schoolId)

        val pointTotal = queryPointHistoryPort.queryBonusAndMinusTotalPointByStudentGcnAndName(
            gcn = pointHistory.studentGcn,
            studentName = pointHistory.studentName
        )

        commandPointHistoryPort.savePointHistory(
            pointHistory.cancelHistory(pointTotal)
        )
        commandPointHistoryPort.deletePointHistory(pointHistory)
    }
}
