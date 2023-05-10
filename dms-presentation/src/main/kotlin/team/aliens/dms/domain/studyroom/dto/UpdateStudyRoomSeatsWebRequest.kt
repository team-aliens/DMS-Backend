package team.aliens.dms.domain.studyroom.dto

import java.util.UUID
import javax.validation.Valid
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

data class UpdateStudyRoomSeatsWebRequest(
    @field:Valid
    val seats: List<SeatWebRequest>
) {
    data class SeatWebRequest(
        @field:NotNull
        @field:Min(0)
        val widthLocation: Int,

        @field:NotNull
        @field:Min(0)
        val heightLocation: Int,

        val number: Int?,

        val typeId: UUID?,

        @field:NotNull
        val status: WebSeatStatus
    )
}
