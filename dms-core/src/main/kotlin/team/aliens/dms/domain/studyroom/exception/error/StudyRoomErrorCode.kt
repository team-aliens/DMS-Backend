package team.aliens.dms.domain.studyroom.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class StudyRoomErrorCode(
    private val status: Int,
    private val message: String,
    private val code: String
) : ErrorProperty {

    STUDY_ROOM_AVAILABLE_SEX_MISMATCH(ErrorStatus.UNAUTHORIZED, "Study Room Available Sex Mismatch", "STUDY-ROOM-401-1"),
    STUDY_ROOM_AVAILABLE_GRADE_MISMATCH(ErrorStatus.UNAUTHORIZED, "Study Room Available Grade Mismatch", "STUDY-ROOM-401-2"),

    STUDY_ROOM_NOT_FOUND(ErrorStatus.NOT_FOUND, "Study Room Not Found", "STUDY-ROOM-404-5"),
    TIME_SLOT_NOT_FOUND(ErrorStatus.NOT_FOUND, "Time Slot Not Found", "STUDY-ROOM-404-6"),
    STUDY_ROOM_TIME_SLOT_NOT_FOUND(ErrorStatus.NOT_FOUND, "Study Room Time Slot Not Found", "STUDY-ROOM-404-7"),

    STUDY_ROOM_ALREADY_EXISTS(ErrorStatus.CONFLICT, "Study Room Already Exists", "STUDY-ROOM-409-4"),
    TIME_SLOT_ALREADY_EXISTS(ErrorStatus.CONFLICT, "Timeslot Already Exists", "STUDY-ROOM-409-5"),
    TIME_SLOT_IN_USE(ErrorStatus.CONFLICT, "Time Slot In Use", "STUDY-ROOM-409-6")
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = code
}
