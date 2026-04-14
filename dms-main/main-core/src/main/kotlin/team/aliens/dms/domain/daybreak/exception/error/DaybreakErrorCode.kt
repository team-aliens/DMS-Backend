package team.aliens.dms.domain.daybreak.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class DaybreakErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    EXIST_DAYBREAK_STUDY_APPLICATION(ErrorStatus.CONFLICT, "Exist Daybreak Study Application", 1),

    NOT_FOUND_DAYBREAK_STUDY_TYPE(ErrorStatus.NOT_FOUND, "Not Found Daybreak Study Type ", 1);

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "DAYBREAK-$status-$sequence"
}
