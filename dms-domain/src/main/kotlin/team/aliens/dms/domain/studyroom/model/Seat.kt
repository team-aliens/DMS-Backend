package team.aliens.dms.domain.studyroom.model

import team.aliens.dms.domain.studyroom.exception.SeatAlreadyAppliedException
import java.util.UUID

data class Seat(

    val id: UUID = UUID(0, 0),

    val studyRoomId: UUID,

    val studentId: UUID?,

    val typeId: UUID?,

    val widthLocation: Int,

    val heightLocation: Int,

    val number: Int?,

    val status: SeatStatus

) {

    fun use(studentId: UUID): Seat {

        if (this.status != SeatStatus.AVAILABLE || this.studentId != null) {
            throw SeatAlreadyAppliedException
        }

        return this.copy(
            studentId = studentId,
            status = SeatStatus.IN_USE
        )
    }

    fun unUse() = this.copy(
        studentId = null,
        status = SeatStatus.AVAILABLE
    )
}
