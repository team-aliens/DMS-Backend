package team.aliens.dms.domain.notification.spi

import team.aliens.dms.domain.notification.model.NotificationOutbox
import java.util.UUID

interface CommandNotificationOutboxPort {

    fun saveNotificationOutbox(notificationOutbox: NotificationOutbox): NotificationOutbox

    fun deleteNotificationOutboxById(id: UUID)
}
