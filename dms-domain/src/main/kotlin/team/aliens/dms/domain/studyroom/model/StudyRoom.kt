package team.aliens.dms.domain.studyroom.model

import java.util.UUID

data class StudyRoom(

    val id: UUID = UUID(0, 0),

    val inUseHeadcount: Int,

    val studyRoomInfoId: UUID,

    val timeSlotId: UUID?

) {
    fun apply() = this.copy(
        inUseHeadcount = inUseHeadcount.inc()
    )

    fun unApply() = this.copy(
        inUseHeadcount = inUseHeadcount.dec()
    )
}
