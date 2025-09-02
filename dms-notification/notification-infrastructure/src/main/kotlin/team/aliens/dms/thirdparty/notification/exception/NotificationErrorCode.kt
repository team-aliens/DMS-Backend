package team.aliens.dms.thirdparty.notification.exception

import team.aliens.dms.common.error.ErrorProperty

enum class NotificationErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "NOTIFICATION-$status-$sequence"
}
