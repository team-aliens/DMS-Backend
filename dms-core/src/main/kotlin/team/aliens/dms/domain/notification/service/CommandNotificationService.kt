package team.aliens.dms.domain.notification.service

import java.util.UUID
import team.aliens.dms.domain.notification.model.DeviceToken

interface CommandNotificationService {

    fun saveDeviceToken(deviceToken: DeviceToken)

    fun deleteNotificationOfUserByUserIdAndId(userId: UUID, notificationOfUserId: UUID)

    fun deleteNotificationOfUserByUserId(userId: UUID)
}