package team.aliens.dms.domain.notification.service

import team.aliens.dms.domain.notification.model.DeviceToken
import java.util.UUID

interface CommandNotificationService {

    fun saveDeviceToken(deviceToken: DeviceToken)

    fun deleteNotificationOfUserByUserIdAndId(userId: UUID, notificationOfUserId: UUID)

    fun deleteNotificationOfUserByUserId(userId: UUID)
}
