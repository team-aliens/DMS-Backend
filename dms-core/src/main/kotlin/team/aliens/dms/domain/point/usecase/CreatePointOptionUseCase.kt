package team.aliens.dms.domain.point.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.point.dto.CreatePointOptionRequest
import team.aliens.dms.domain.point.dto.CreatePointOptionResponse
import team.aliens.dms.domain.point.exception.PointOptionNameExistsException
import team.aliens.dms.domain.point.model.PointOption
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.spi.CommandPointOptionPort
import team.aliens.dms.domain.point.spi.QueryPointOptionPort
import team.aliens.dms.domain.user.service.UserService

@UseCase
class CreatePointOptionUseCase(
    private val userService: UserService,
    private val commandPointOptionPort: CommandPointOptionPort,
    private val queryPointOptionPort: QueryPointOptionPort
) {

    fun execute(request: CreatePointOptionRequest): CreatePointOptionResponse {

        val user = userService.getCurrentUser()

        if (queryPointOptionPort.existByNameAndSchoolId(request.name, user.schoolId)) {
            throw PointOptionNameExistsException
        }

        val pointType = PointType.valueOf(request.type)
        val pointOption = commandPointOptionPort.savePointOption(
            PointOption(
                schoolId = user.schoolId,
                name = request.name,
                score = request.score,
                type = pointType
            )
        )

        return CreatePointOptionResponse(pointOption.id)
    }
}
