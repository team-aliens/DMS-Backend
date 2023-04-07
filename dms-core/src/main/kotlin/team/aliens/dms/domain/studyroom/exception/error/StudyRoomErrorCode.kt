package team.aliens.dms.domain.studyroom.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class StudyRoomErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    STUDY_ROOM_AVAILABLE_SEX_MISMATCH(ErrorStatus.UNAUTHORIZED, "Study Room Available Sex Mismatch"),
    STUDY_ROOM_AVAILABLE_GRADE_MISMATCH(ErrorStatus.UNAUTHORIZED, "Study Room Available Grade Mismatch"),

    STUDY_ROOM_NOT_FOUND(ErrorStatus.NOT_FOUND, "Study Room Not Found"),
    TIME_SLOT_NOT_FOUND(ErrorStatus.NOT_FOUND, "Time Slot Not Found"),
    STUDY_ROOM_TIME_SLOT_NOT_FOUND(ErrorStatus.NOT_FOUND, "Study Room Time Slot Not Found"),

    STUDY_ROOM_ALREADY_EXISTS(ErrorStatus.CONFLICT, "Study Room Already Exists"),
    TIME_SLOT_ALREADY_EXISTS(ErrorStatus.CONFLICT, "Timeslot Already Exists"),
    TIME_SLOT_IN_USE(ErrorStatus.CONFLICT, "Time Slot In Use")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}
