package team.aliens.dms.domain.outing.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.outing.model.OutingType
import team.aliens.dms.domain.outing.service.OutingService

@UseCase
class RemoveOutingTypeUseCase(
    private val outingService: OutingService,
    private val securityService: SecurityService
) {

    fun execute(title: String) {
        val schoolId = securityService.getCurrentSchoolId()

        val outingType = outingService.getOutingType(
            OutingType(title, schoolId)
        )

        outingService.deleteOutingType(outingType)
    }
}
