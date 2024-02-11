package team.aliens.dms.domain.outing.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.outing.dto.CreateOutingTypeRequest
import team.aliens.dms.domain.outing.model.OutingType
import team.aliens.dms.domain.outing.service.OutingService
import team.aliens.dms.domain.user.service.UserService

@UseCase
class CreateOutingTypeUseCase(
    private val userService: UserService,
    private val outingService: OutingService
) {

    fun execute(request: CreateOutingTypeRequest) {
        val user = userService.getCurrentUser()

        val outingType = OutingType(
            title = request.title,
            schoolId = user.schoolId
        )

        outingService.checkOutingTypeExists(outingType)

        outingService.saveOutingType(outingType)
    }
}