package team.aliens.dms.domain.manager.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class ManagerErrorCode(
    private val status: Int,
    private val message: String,
    private val code: String
) : ErrorProperty {

    MANAGER_INFO_NOT_MATCHED(ErrorStatus.UNAUTHORIZED, "Manager Info Not Matched", "MANAGER-401-1"),

    MANAGER_NOT_FOUND(ErrorStatus.NOT_FOUND, "Manager Not Found", "MANAGER-404-1")
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = code
}
