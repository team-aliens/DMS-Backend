package team.aliens.dms.domain.notification.service

import java.util.UUID

interface CheckNotificationService {

    fun checkDeviceTokenByUserId(userId: UUID): Boolean
}
