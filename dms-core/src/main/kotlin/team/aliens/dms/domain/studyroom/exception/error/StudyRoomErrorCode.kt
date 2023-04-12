package team.aliens.dms.domain.studyroom.exception.error

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class StudyRoomErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    STUDY_ROOM_AVAILABLE_SEX_MISMATCH(ErrorStatus.UNAUTHORIZED, "Study Room Available Sex Mismatch", 1),
    STUDY_ROOM_AVAILABLE_GRADE_MISMATCH(ErrorStatus.UNAUTHORIZED, "Study Room Available Grade Mismatch", 2),

    STUDY_ROOM_NOT_FOUND(ErrorStatus.NOT_FOUND, "Study Room Not Found", 5),
    TIME_SLOT_NOT_FOUND(ErrorStatus.NOT_FOUND, "Time Slot Not Found", 6),
    STUDY_ROOM_TIME_SLOT_NOT_FOUND(ErrorStatus.NOT_FOUND, "Study Room Time Slot Not Found", 7),

    STUDY_ROOM_ALREADY_EXISTS(ErrorStatus.CONFLICT, "Study Room Already Exists", 4),
    TIME_SLOT_ALREADY_EXISTS(ErrorStatus.CONFLICT, "Timeslot Already Exists", 5),
    TIME_SLOT_IN_USE(ErrorStatus.CONFLICT, "Time Slot In Use", 6)
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "STUDY-ROOM-$status-$sequence"
}
