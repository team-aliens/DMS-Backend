package team.aliens.dms.domain.studyroom.dto

import team.aliens.dms.common.validator.NotNullElements
import java.util.UUID
import javax.validation.Valid
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class UpdateStudyRoomWebRequest(

    @field:NotNull
    @field:Min(1)
    val floor: Int?,

    @field:NotBlank
    @field:Size(max = 10)
    val name: String?,

    @field:NotNull
    @field:Min(0)
    val totalWidthSize: Int?,

    @field:NotNull
    @field:Min(0)
    val totalHeightSize: Int?,

    @field:NotBlank
    @field:Size(max = 20)
    val eastDescription: String?,

    @field:NotBlank
    @field:Size(max = 20)
    val westDescription: String?,

    @field:NotBlank
    @field:Size(max = 20)
    val southDescription: String?,

    @field:NotBlank
    @field:Size(max = 20)
    val northDescription: String?,

    @field:NotNull
    val availableSex: WebSex?,

    @field:NotNull
    @field:Min(0)
    val availableGrade: Int?,

    @field:NotNullElements
    val timeSlotIds: List<UUID>?,

    @field:Valid
    val seats: List<SeatRequest>

) {

    data class SeatRequest(

        @field:NotNull
        @field:Min(0)
        val widthLocation: Int?,

        @field:NotNull
        @field:Min(0)
        val heightLocation: Int?,

        val number: Int?,

        val typeId: UUID?,

        @field:NotNull
        val status: WebSeatStatus?

    )
}
