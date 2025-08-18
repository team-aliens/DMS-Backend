package team.aliens.dms.domain.manager.exception

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class ManagerErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    MANAGER_INFO_NOT_MATCHED(ErrorStatus.UNAUTHORIZED, "Manager Info Not Matched", 1)
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "MANAGER-$status-$sequence"
}
