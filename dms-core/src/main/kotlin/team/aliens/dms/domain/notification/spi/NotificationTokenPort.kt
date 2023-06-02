package team.aliens.dms.domain.notification.spi

import team.aliens.dms.domain.notification.model.DeviceToken
import java.util.UUID

interface NotificationTokenPort {

    fun saveNotificationToken(deviceToken: DeviceToken): DeviceToken

    fun deleteNotificationTokenById(userId: UUID)
    
    fun queryNotificationTokenById(userId: UUID): DeviceToken?
}
