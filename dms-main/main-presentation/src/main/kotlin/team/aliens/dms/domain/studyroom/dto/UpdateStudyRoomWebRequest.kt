package team.aliens.dms.domain.studyroom.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import team.aliens.dms.common.validator.NotNullElements
import java.util.UUID

data class UpdateStudyRoomWebRequest(

    @field:NotNull
    @field:Min(1)
    val floor: Int,

    @field:NotBlank
    @field:Size(max = 10)
    val name: String,

    @field:NotNull
    @field:Min(0)
    val totalWidthSize: Int,

    @field:NotNull
    @field:Min(0)
    val totalHeightSize: Int,

    @field:NotBlank
    @field:Size(max = 20)
    val eastDescription: String,

    @field:NotBlank
    @field:Size(max = 20)
    val westDescription: String,

    @field:NotBlank
    @field:Size(max = 20)
    val southDescription: String,

    @field:NotBlank
    @field:Size(max = 20)
    val northDescription: String,

    @field:NotNull
    val availableSex: WebSex,

    @field:NotNull
    @field:Min(0)
    val availableGrade: Int,

    @field:Size(min = 1)
    @field:NotNullElements
    val timeSlotIds: List<UUID>,

    @field:Valid
    val seats: List<SeatRequest>

) {

    data class SeatRequest(

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
