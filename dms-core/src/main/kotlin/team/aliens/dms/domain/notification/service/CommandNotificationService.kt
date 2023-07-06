package team.aliens.dms.domain.notification.service

import team.aliens.dms.domain.notification.model.DeviceToken
import java.util.UUID

interface CommandNotificationService {

    fun createOrUpdateDeviceToken(deviceToken: DeviceToken)

    fun deleteDeviceTokenById(deviceTokenId: UUID)

    fun deleteNotificationOfUserByUserIdAndId(userId: UUID, notificationOfUserId: UUID)

    fun deleteNotificationOfUserByUserId(userId: UUID)
}
