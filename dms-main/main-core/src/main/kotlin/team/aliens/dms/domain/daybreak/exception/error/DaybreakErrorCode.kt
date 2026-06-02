package team.aliens.dms.domain.daybreak.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class DaybreakErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    EXIST_DAYBREAK_STUDY_APPLICATION(ErrorStatus.CONFLICT, "Exist Daybreak Study Application", 1),
    EXIST_DAYBREAK_STUDY_TYPE(ErrorStatus.CONFLICT, "Exist Daybreak Type of Daybreak", 2),

    NOT_FOUND_DAYBREAK_STUDY_APPLICATION(ErrorStatus.NOT_FOUND, "Not Found Daybreak Study Application", 1),
    NOT_FOUND_DAYBREAK_STUDY_TYPE(ErrorStatus.NOT_FOUND, "Not Found Daybreak Study Type ", 1),

    DAYBREAK_START_DATE_AFTER_END_DATE(ErrorStatus.BAD_REQUEST, "Start Date Cannot Be After End Date", 1),
    DAYBREAK_PAST_DATE(ErrorStatus.BAD_REQUEST, "Daybreak Application Date Cannot Be In The Past", 2),
    DAYBREAK_INVALID_DATE_RANGE(ErrorStatus.BAD_REQUEST, "Daybreak Application Date Must Be Within Monday To Thursday", 3);

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "DAYBREAK-$status-$sequence"
}
