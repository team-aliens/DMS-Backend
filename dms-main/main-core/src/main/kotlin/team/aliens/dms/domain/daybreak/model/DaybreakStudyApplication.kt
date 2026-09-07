package team.aliens.dms.domain.daybreak.model

import team.aliens.dms.common.annotation.Aggregate
import team.aliens.dms.common.model.SchoolIdDomain
import team.aliens.dms.domain.auth.model.Authority
import team.aliens.dms.domain.daybreak.exception.DaybreakInvalidDateRangeException
import team.aliens.dms.domain.daybreak.exception.DaybreakPastDateException
import team.aliens.dms.domain.daybreak.exception.DaybreakStartDateAfterEndDateException
import team.aliens.dms.domain.daybreak.exception.DaybreakStudyApplicationCanNotRevertException
import team.aliens.dms.domain.user.exception.InvalidRoleException
import java.time.DayOfWeek
import java.time.LocalDate
import java.util.UUID

// 상태가 변하는 엔티티라 data class가 아니다(값 기준 equals가 어긋나고, private set을 쓸 수 없다).
// equals/hashCode는 의도적으로 재정의하지 않고 identity 비교를 쓴다 — 이 리포의 JPA 엔티티도 전부 그렇고,
@Aggregate
class DaybreakStudyApplication(

    val id: UUID = UUID(0, 0),

    val studyTypeId: UUID,

    val startDate: LocalDate,

    val endDate: LocalDate,

    val reason: String,

    status: Status,

    // 되돌리기를 위해 직전 상태를 보관한다. 상태 변경 이력이 없으면 null
    previousStatus: Status? = null,

    val teacherId: UUID,

    val studentId: UUID,

    override val schoolId: UUID

) : SchoolIdDomain {

    // 상태 전이는 아래 메서드로만 한다. 외부에서 직접 대입할 수 없도록 setter를 막았다.
    var status: Status = status
        private set

    var previousStatus: Status? = previousStatus
        private set

    companion object {
        /**
         * 새벽자습은 월~목만 운영하므로, 신청 대상도 한 주의 월~목 구간으로 제한한다.
         *
         * 오늘이 월~목이면 이번 주 월~목, 금~일이면 다음 주 월~목이 대상 구간이다
         * (금요일부터는 이번 주 운영이 끝나 신청할 수 있는 날이 남아있지 않다).
         *
         * 검증 순서에 의미가 있다 — 과거 검사가 구간 검사보다 앞이라, 대상 구간 안이지만
         * 이미 지난 요일을 신청하면 DaybreakInvalidDateRangeException이 아니라
         * DaybreakPastDateException이 나간다.
         */
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

            // 금~일이면 이번 주는 이미 끝났으므로 다음 주 월요일을 기준으로 잡는다
            val monday = if (today.dayOfWeek >= DayOfWeek.FRIDAY)
                today.with(DayOfWeek.MONDAY).plusWeeks(1)
            else
                today.with(DayOfWeek.MONDAY)
            val thursday = monday.plusDays(3)

            // 1) 시작일이 종료일보다 뒤  2) 과거 날짜(오늘은 허용)  3) 대상 주의 월~목 밖
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

        this.previousStatus = this.status
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

    // 최종 승인(SECOND_APPROVED)/거절(REJECTED)을 직전 상태로 되돌린다.
    fun revert() {
        if (!isTerminalStatus()) throw DaybreakStudyApplicationCanNotRevertException

        val previous = previousStatus ?: throw DaybreakStudyApplicationCanNotRevertException

        this.status = previous
        this.previousStatus = null
    }

    // 스케줄러가 기한이 지난 신청을 만료 처리할 때 사용
    fun expire() {
        this.previousStatus = this.status
        this.status = Status.EXPIRED
    }

    // REJECTED, FIRST_APPROVED, SECOND_APPROVED만 알림을 발송함
    fun getTitle(): String =
        when (status) {
            Status.REJECTED -> "새벽 자습 신청이 거절되었습니다"
            Status.FIRST_APPROVED -> "새벽 자습 신청이 1차 승인되었습니다."
            Status.SECOND_APPROVED -> "새벽 자습 신청이 최종 승인되었습니다."
            else -> ""
        }
}
