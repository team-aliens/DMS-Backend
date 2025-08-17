package team.aliens.dms.domain.outing.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.outing.dto.request.CreateOutingTypeRequest
import team.aliens.dms.domain.outing.model.OutingType
import team.aliens.dms.domain.outing.service.OutingService

@UseCase
class CreateOutingTypeUseCase(
    private val outingService: OutingService,
    private val securityService: SecurityService,
) {

    fun execute(request: CreateOutingTypeRequest) {
        val outingType = OutingType(
            title = request.title,
            schoolId = securityService.getCurrentSchoolId()
        )

        outingService.checkOutingTypeExists(outingType)

        outingService.saveOutingType(outingType)
    }
}
