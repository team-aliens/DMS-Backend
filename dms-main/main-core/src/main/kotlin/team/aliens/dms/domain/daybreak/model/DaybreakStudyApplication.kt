package team.aliens.dms.domain.daybreak.model

import team.aliens.dms.common.annotation.Aggregate
import team.aliens.dms.common.model.SchoolIdDomain
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.daybreak.exception.DaybreakInvalidDateRangeException
import team.aliens.dms.domain.daybreak.exception.DaybreakPastDateException
import team.aliens.dms.domain.daybreak.exception.DaybreakStartDateAfterEndDateException
import team.aliens.dms.domain.user.exception.InvalidRoleException
import java.time.DayOfWeek
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

    companion object {
        fun create(
            studyTypeId: UUID,
            startDate: LocalDate,
            endDate: LocalDate,
            reason: String,
            status: Status,
            teacherId: UUID,
            studentId: UUID,
            schoolId: UUID
        ): DaybreakStudyApplication {
            val today = LocalDate.now()
            val monday = today.with(DayOfWeek.MONDAY)
            val thursday = monday.plusDays(3)

            if (startDate > endDate) throw DaybreakStartDateAfterEndDateException
            if (startDate < today || endDate < today) throw DaybreakPastDateException
            if (startDate < monday || endDate > thursday) throw DaybreakInvalidDateRangeException

            return DaybreakStudyApplication(
                studyTypeId = studyTypeId,
                startDate = startDate,
                endDate = endDate,
                reason = reason,
                status = status,
                teacherId = teacherId,
                studentId = studentId,
                schoolId = schoolId
            )
        }
    }

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

    // REJECTED와 SECOND_APPROVED만 알림을 발송함
    fun getTitle(): String =
        when (status) {
            Status.REJECTED -> "새벽 자습 신청이 거절되었습니다"
            Status.SECOND_APPROVED -> "새벽 자습 신청이 승인되었습니다"
            else -> ""
        }
}
