package team.aliens.dms.domain.point.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.point.dto.CreatePointOptionRequest
import team.aliens.dms.domain.point.dto.CreatePointOptionResponse
import team.aliens.dms.domain.point.model.PointOption
import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.point.service.PointService
import team.aliens.dms.domain.user.service.UserService
import java.time.LocalDateTime

@UseCase
class CreatePointOptionUseCase(
    private val userService: UserService,
    private val pointService: PointService
) {

    fun execute(request: CreatePointOptionRequest): CreatePointOptionResponse {

        val user = userService.getCurrentUser()

        val pointOption = pointService.savePointOption(
            PointOption(
                schoolId = user.schoolId,
                name = request.name,
                score = request.score,
                type = PointType.valueOf(request.type),
                createdAt = LocalDateTime.now()

            )
        )

        return CreatePointOptionResponse(pointOption.id)
    }
}
