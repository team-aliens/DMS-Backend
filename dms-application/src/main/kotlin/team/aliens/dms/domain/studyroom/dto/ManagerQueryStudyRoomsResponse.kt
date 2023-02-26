package team.aliens.dms.domain.studyroom.dto

import team.aliens.dms.domain.student.model.Sex
import java.util.UUID

data class ManagerQueryStudyRoomsResponse(
    val studyRooms: List<StudyRoomElement>
) {

    data class StudyRoomElement(
        val id: UUID,
        val floor: Int,
        val name: String,
        val availableGrade: Int,
        val availableSex: Sex,
        val inUseHeadcount: Int,
        val totalAvailableSeat: Int
    )
}
