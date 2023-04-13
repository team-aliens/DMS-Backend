package team.aliens.dms.domain.point.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.spi.SecurityPort
import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.manager.spi.QueryManagerPort
import team.aliens.dms.domain.point.exception.PointOptionNotFoundException
import team.aliens.dms.domain.point.spi.CommandPointOptionPort
import team.aliens.dms.domain.point.spi.QueryPointOptionPort
import java.util.UUID

@UseCase
class RemovePointOptionUseCase(
    private val securityPort: SecurityPort,
    private val queryManagerPort: QueryManagerPort,
    private val queryPointOptionPort: QueryPointOptionPort,
    private val commandPointOptionPort: CommandPointOptionPort
) {

    fun execute(pointOptionId: UUID) {
        val currentUserId = securityPort.getCurrentUserId()
        val manager = queryManagerPort.queryManagerById(currentUserId) ?: throw ManagerNotFoundException

        val pointOption = queryPointOptionPort.queryPointOptionById(pointOptionId) ?: throw PointOptionNotFoundException
        pointOption.checkSchoolId(manager.schoolId)

        commandPointOptionPort.deletePointOption(pointOption)
    }
}
