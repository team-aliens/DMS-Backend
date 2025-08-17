package team.aliens.dms.domain.volunteer.exception

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class VolunteerErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    VOLUNTEER_NOT_AVAILABLE(ErrorStatus.FORBIDDEN, "Volunteer Not Available", 1),

    VOLUNTEER_APPLICATION_NOT_FOUND(ErrorStatus.NOT_FOUND, "Volunteer Application Not Found", 1),
    VOLUNTEER_NOT_FOUND(ErrorStatus.NOT_FOUND, "Volunteer Not Found", 2),

    VOLUNTEER_APPLICATION_ALREADY_ASSIGNED(ErrorStatus.CONFLICT, "Volunteer Application Already Assigned", 1),
    VOLUNTEER_APPLICATION_NOT_ASSIGNED(ErrorStatus.CONFLICT, "Volunteer Application Not Assigned", 2),
    VOLUNTEER_APPLICATION_ALREADY_EXISTS(ErrorStatus.CONFLICT, "Volunteer Application Already Exists", 3)
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "Volunteer-$status-$sequence"
}
