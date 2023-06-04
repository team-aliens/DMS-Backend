package team.aliens.dms.domain.studyroom.dto

import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.studyroom.model.Seat
import team.aliens.dms.domain.studyroom.model.SeatStatus
import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.model.StudyRoomTimeSlot
import java.util.UUID
import java.util.function.Function

data class UpdateStudyRoomRequest(
    val floor: Int,
    val name: String,
    val totalWidthSize: Int,
    val totalHeightSize: Int,
    val eastDescription: String,
    val westDescription: String,
    val southDescription: String,
    val northDescription: String,
    val availableSex: String,
    val availableGrade: Int,
    val timeSlotIds: List<UUID>,
    val seats: List<SeatRequest>
) {

    data class SeatRequest(
        val widthLocation: Int,
        val heightLocation: Int,
        val number: Int?,
        val typeId: UUID?,
        val status: String,
    )

    fun toStudyRoomUpdateFunction() =
        Function<StudyRoom, StudyRoom> {
            it.copy(
                name = name,
                floor = floor,
                widthSize = totalWidthSize,
                heightSize = totalHeightSize,
                availableHeadcount = seats.count {
                    SeatStatus.AVAILABLE == SeatStatus.valueOf(it.status)
                },
                availableSex = Sex.valueOf(availableSex),
                availableGrade = availableGrade,
                eastDescription = eastDescription,
                westDescription = westDescription,
                southDescription = southDescription,
                northDescription = northDescription
            )
        }

    fun toStudyRoomTimeSlots(studyRoomId: UUID) =
        timeSlotIds.map {
            StudyRoomTimeSlot(
                studyRoomId = studyRoomId,
                timeSlotId = it
            )
        }

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
