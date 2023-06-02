package team.aliens.dms.thirdparty.notification.exception

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class NotificationErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    NOTIFICATION_PROCESS_FAILED(ErrorStatus.BAD_REQUEST, "Notification process failed", 1)
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "NOTIFICATION-$status-$sequence"
}
