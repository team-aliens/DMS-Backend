package team.aliens.dms.domain.notification.usecase

import team.aliens.dms.common.annotation.UseCase
import team.aliens.dms.domain.notification.dto.RemoveDeviceTokenRequest
import team.aliens.dms.domain.notification.service.NotificationService
import team.aliens.dms.domain.user.service.UserService

@UseCase
class RemoveDeviceTokenUseCase(
    private val userService: UserService,
    private val notificationService: NotificationService
) {
    fun execute(request: RemoveDeviceTokenRequest) {
        val user = userService.getCurrentUser()

        val deviceToken = notificationService.getDeviceTokenByOSAndDeviceIdAndUserId(
            operatingSystem = request.operatingSystem,
            deviceId = request.deviceId,
            userId = user.id
        )
        notificationService.deleteDeviceTokenById(deviceToken.id)
    }
}
