package team.aliens.dms.domain.studyroom.model

import java.util.UUID

data class Seat(

    val id: UUID = UUID(0, 0),

    val studyRoomId: UUID,

    val typeId: UUID?,

    val widthLocation: Int,

    val heightLocation: Int,

    val number: Int?,

    val status: SeatStatus

) {
    companion object {
        fun processFullName(seatNumber: Int, typeName: String?) =
            " ${seatNumber}번 ${typeName ?: "일반"}자리"
    }
}
