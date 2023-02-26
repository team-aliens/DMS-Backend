package team.aliens.dms.domain.manager.error

import team.aliens.dms.common.error.ErrorProperty

enum class ManagerErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    MANAGER_INFO_NOT_MATCHED(401, "Manager Info Not Matched"),

    MANAGER_NOT_FOUND(404, "Manager Not Found")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}
