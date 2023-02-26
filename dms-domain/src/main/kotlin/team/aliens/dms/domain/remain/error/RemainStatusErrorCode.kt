package team.aliens.dms.domain.remain.error

import team.aliens.dms.common.error.ErrorProperty

enum class RemainStatusErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    REMAIN_STATUS_NOT_FOUND(404, "Remain Status Not Found")
    ;

    override fun status() = status
    override fun message() = message
}
