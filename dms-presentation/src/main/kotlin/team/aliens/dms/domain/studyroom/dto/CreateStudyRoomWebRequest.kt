package team.aliens.dms.domain.studyroom.dto

import java.util.UUID
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class CreateStudyRoomWebRequest(

    @field:NotNull
    val floor: Int,

    @field:NotBlank
    val name: String,

    @field:NotNull
    val totalWidthSize: Int,

    @field:NotNull
    val totalHeightSize: Int,

    @field:NotBlank
    val eastDescription: String,

    @field:NotBlank
    val westDescription: String,

    @field:NotBlank
    val southDescription: String,

    @field:NotBlank
    val northDescription: String,

    @field:NotBlank
    val availableSex: String,

    @field:NotNull
    val availableGrade: Int,

    val seats: List<SeatRequest>
) {
    data class SeatRequest(

        @field:NotNull
        val widthLocation: Int,

        @field:NotNull
        val heightLocation: Int,

        val number: Int?,
        val typeId: UUID?,

        @field:NotNull
        val status: WebSeatStatus
    )
}
