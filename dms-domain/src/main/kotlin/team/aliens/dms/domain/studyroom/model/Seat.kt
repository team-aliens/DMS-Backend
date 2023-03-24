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
        fun processName(
            seatNumber: Int,
            typeName: String,
            seatNameType: SeatNameType?
        ) = when (seatNameType) {
            SeatNameType.SHORT, null -> "$seatNumber"
            SeatNameType.LONG -> "${seatNumber}번 ${typeName}자리"
        }
    }
}
