package team.aliens.dms.domain.remain.error

import team.aliens.dms.common.error.ErrorProperty

enum class RemainAvailableTimeErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    REMAIN_AVAILABLE_TIME_NOT_FOUND(404, "Remain Available Time Not Found")
    ;

    override fun status() = status
    override fun message() = message
}