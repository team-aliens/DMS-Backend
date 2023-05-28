package team.aliens.dms.domain.studyroom.dto

import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.SeatStatus
import java.util.UUID

data class UpdateStudyRoomSeatsRequest(
    val seats: List<SeatRequest>
) {
    val availableHeadCount: Int = seats.count {
        SeatStatus.AVAILABLE == SeatStatus.valueOf(it.status)
    }

    data class SeatRequest(
        val widthLocation: Int,
        val heightLocation: Int,
        val number: Int?,
        val typeId: UUID?,
        val status: String
    )

    fun toSeats(studyRoomId: UUID) =
        seats.map {
            Seat(
                studyRoomId = studyRoomId,
                typeId = it.typeId,
                widthLocation = it.widthLocation,
                heightLocation = it.heightLocation,
                number = it.number,
                status = SeatStatus.valueOf(it.status)
            )
        }
}
