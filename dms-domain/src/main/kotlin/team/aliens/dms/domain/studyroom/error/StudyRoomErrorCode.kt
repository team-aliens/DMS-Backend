package team.aliens.dms.domain.studyroom.error

import team.aliens.dms.common.error.ErrorProperty

enum class StudyRoomErrorCode(
    private val status: Int,
    private val message: String
) : ErrorProperty {

    STUDY_ROOM_AVAILABLE_SEX_MISMATCH(401, "Study Room Available Sex Mismatch"),
    STUDY_ROOM_AVAILABLE_GRADE_MISMATCH(401, "Study Room Available Grade Mismatch"),

    STUDY_ROOM_NOT_FOUND(404, "Study Room Not Found"),

    STUDY_ROOM_ALREADY_EXISTS(409, "Study Room Already Exists")
    ;

    override fun status(): Int = status
    override fun message(): String = message
}
