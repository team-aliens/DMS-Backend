package team.aliens.dms.domain.notification.exception

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class NotificationErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    DEVICE_TOKEN_NOT_FOUND(ErrorStatus.BAD_REQUEST, "Notification Token Not Found", 1)
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "NOTIFICATION-$status-$sequence"
}
