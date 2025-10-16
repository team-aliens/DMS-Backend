package team.aliens.dms.domain.point.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.point.service.PointService
import team.aliens.dms.domain.user.service.UserService
import java.util.UUID

@UseCase
class RemovePointOptionUseCase(
    private val userService: UserService,
    private val pointService: PointService
) {

    fun execute(pointOptionId: UUID) {

        val user = userService.getCurrentUser()

        val pointOption = pointService.getPointOptionById(pointOptionId, user.schoolId)

        pointService.deletePointOption(pointOption)
    }
}
