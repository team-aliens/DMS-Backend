package team.aliens.dms.domain.point.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.point.exception.PointHistoryNotFoundException
import team.aliens.dms.domain.point.spi.CommandPointHistoryPort
import team.aliens.dms.domain.point.spi.QueryPointHistoryPort
import team.aliens.dms.domain.school.validateSameSchool
import team.aliens.dms.domain.user.exception.UserNotFoundException
import team.aliens.dms.domain.user.spi.QueryUserPort
import java.util.UUID

@UseCase
class CancelGrantedPointUseCase(
    private val commandPointHistoryPort: CommandPointHistoryPort,
    private val queryPointHistoryPort: QueryPointHistoryPort,
    private val securityPort: SecurityPort,
    private val queryUserPort: QueryUserPort
) {

    fun execute(pointHistoryId: UUID) {
        val currentUserId = securityPort.getCurrentUserId()
        val manager = queryUserPort.queryUserById(currentUserId) ?: throw UserNotFoundException

        val pointHistory = queryPointHistoryPort.queryPointHistoryById(pointHistoryId)
            ?: throw PointHistoryNotFoundException

        validateSameSchool(manager.schoolId, pointHistory.schoolId)

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
