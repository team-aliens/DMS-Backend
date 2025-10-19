package team.aliens.dms.domain.notification.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.common.service.security.SecurityService
import team.aliens.dms.domain.notification.dto.SetDeviceTokenRequest
import team.aliens.dms.domain.notification.service.NotificationService

@UseCase
class SetDeviceTokenUseCase(
    private val securityService: SecurityService,
    private val notificationService: NotificationService
) {

    fun execute(request: SetDeviceTokenRequest) {
        val userId = securityService.getCurrentUserId()
        val schoolId = securityService.getCurrentSchoolId()

        notificationService.saveDeviceToken(
            request.toDeviceToken(userId, schoolId)
        )
    }
}
