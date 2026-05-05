package team.aliens.dms.domain.daybreak.model

import team.aliens.dms.common.annotation.Aggregate
import team.aliens.dms.common.model.SchoolIdDomain
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.user.exception.InvalidRoleException
import java.time.LocalDate
import java.util.UUID

@Aggregate
data class DaybreakStudyApplication(

    val id: UUID = UUID(0, 0),

    val studyTypeId: UUID,

    val startDate: LocalDate,

    val endDate: LocalDate,

    val reason: String,

    var status: Status,

    val teacherId: UUID,

    val studentId: UUID,

    override val schoolId: UUID

) : SchoolIdDomain {

    fun changeStatus(authority: Authority, newStatus: Status) {
        if (isTerminalStatus()) throw InvalidRoleException

        when (authority) {
            Authority.GENERAL_TEACHER -> validateGeneralTeacherTransition(newStatus)
            Authority.HEAD_TEACHER -> validateHeadTeacherTransition(newStatus)
            else -> throw InvalidRoleException
        }

        this.status = newStatus
    }

    private fun isTerminalStatus() =
        status == Status.REJECTED || status == Status.SECOND_APPROVED

    private fun validateGeneralTeacherTransition(newStatus: Status) {
        if (this.status != Status.PENDING) throw InvalidRoleException

        if (newStatus != Status.FIRST_APPROVED && newStatus != Status.REJECTED) throw InvalidRoleException
    }

    private fun validateHeadTeacherTransition(newStatus: Status) {
        if (status != Status.FIRST_APPROVED) throw InvalidRoleException
        if (newStatus != Status.SECOND_APPROVED && newStatus != Status.REJECTED) throw InvalidRoleException
    }
}
