package team.aliens.dms.domain.notification.service

import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.notification.model.NotificationOfUser
import java.util.UUID

interface CommandNotificationService {

    fun saveDeviceToken(deviceToken: DeviceToken): DeviceToken

    fun deleteNotificationOfUserByUserIdAndId(userId: UUID, notificationOfUserId: UUID)

    fun deleteNotificationOfUserByUserId(userId: UUID)

    fun saveNotificationOfUserByUserId(
        userId: UUID,
        notificationOfUser: NotificationOfUser
    ): NotificationOfUser

    fun saveNotificationsOfUserByUserId(
        userId: UUID,
        notificationOfUsers: List<NotificationOfUser>
    )
}
