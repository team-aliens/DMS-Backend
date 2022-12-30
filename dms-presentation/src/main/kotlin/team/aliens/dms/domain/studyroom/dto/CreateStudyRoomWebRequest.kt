package team.aliens.dms.domain.studyroom.dto

import java.util.UUID
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class CreateStudyRoomWebRequest(

    @field:NotNull
    @field:Size(min = 1)
    val floor: Int?,

    @field:NotBlank
    @field:Size(max = 10)
    val name: String?,

    @field:NotNull
    @field:Size(min = 0)
    val totalWidthSize: Int?,

    @field:NotNull
    @field:Size(min = 0)
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

    @field:NotBlank
    @field:Size(min = 3, max = 6)
    val availableSex: WebSex?,

    @field:NotNull
    @field:Size(min = 0)
    val availableGrade: Int?,

    val seats: List<SeatRequest>

) {

    data class SeatRequest(

        @field:NotNull
        @field:Size(min = 0)
        val widthLocation: Int?,

        @field:NotNull
        @field:Size(min = 0)
        val heightLocation: Int?,

        val number: Int?,

        val typeId: UUID?,

        @field:NotNull
        @field:Size(min = 5, max = 11)
        val status: WebSeatStatus?

    )
}
