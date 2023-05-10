package team.aliens.dms.domain.studyroom.dto

import team.aliens.dms.domain.student.model.Sex
import team.aliens.dms.domain.studyroom.model.StudyRoom

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
    val availableGrade: Int
) {

    fun toStudyRoom(studyRoom: StudyRoom) =
        studyRoom.copy(
            name = name,
            floor = floor,
            widthSize = totalWidthSize,
            heightSize = totalHeightSize,
            availableSex = Sex.valueOf(availableSex),
            availableGrade = availableGrade,
            eastDescription = eastDescription,
            westDescription = westDescription,
            southDescription = southDescription,
            northDescription = northDescription
        )
}
