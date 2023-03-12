package team.aliens.dms.domain.studyroom.model

import java.util.UUID

data class StudyRoom(

    val seatId: UUID,

    val timeSlotId: UUID,

    val studentId: UUID
) {
    fun apply() = this.copy(
        inUseHeadcount = inUseHeadcount.inc()
    )

    fun unApply() = this.copy(
        inUseHeadcount = inUseHeadcount.dec()
    )
}
