package team.aliens.dms.domain.point.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.manager.exception.ManagerNotFoundException
import team.aliens.dms.domain.point.dto.CreatePointOptionRequest
import team.aliens.dms.domain.point.exception.PointOptionNameExistsException
import team.aliens.dms.domain.point.model.PointOption
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.CommandPointOptionPort
import team.aliens.dms.domain.point.spi.PointQueryManagerPort
import team.aliens.dms.domain.point.spi.PointSecurityPort
import team.aliens.dms.domain.point.spi.QueryPointOptionPort
import java.util.UUID

@UseCase
class CreatePointOptionUseCase(
    private val securityPort: PointSecurityPort,
    private val queryManagerPort: PointQueryManagerPort,
    private val commandPointOptionPort: CommandPointOptionPort,
    private val queryPointOptionPort: QueryPointOptionPort
) {

    fun execute(request: CreatePointOptionRequest): UUID {
        val currentUserId = securityPort.getCurrentUserId()
        val manager = queryManagerPort.queryManagerById(currentUserId) ?: throw ManagerNotFoundException

        if(queryPointOptionPort.existByNameAndSchoolId(request.name, manager.schoolId)) {
            throw PointOptionNameExistsException
        }

        return commandPointOptionPort.savePointOption(
            PointOption(
                schoolId = manager.schoolId,
                name = request.name,
                score = request.score,
                type = PointType.valueOf(request.type)
            )
        )
    }
}