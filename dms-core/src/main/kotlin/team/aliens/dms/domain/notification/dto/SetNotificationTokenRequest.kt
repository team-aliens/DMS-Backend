package team.aliens.dms.domain.notification.dto

import team.aliens.dms.domain.notification.model.DeviceToken
import team.aliens.dms.domain.user.model.User

class SetNotificationTokenRequest(
    val deviceToken: String
) {
    fun toNotificationToken(user: User) = DeviceToken(
        userId = user.id,
        schoolId = user.schoolId,
        deviceToken = deviceToken
    )
}
