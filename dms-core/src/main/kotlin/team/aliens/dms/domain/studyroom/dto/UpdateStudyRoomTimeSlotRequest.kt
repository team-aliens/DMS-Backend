package team.aliens.dms.domain.studyroom.dto

import team.aliens.dms.domain.studyroom.model.StudyRoomTimeSlot
import java.util.UUID

data class UpdateStudyRoomTimeSlotRequest(
    val timeSlotIds: List<UUID>
) {
    fun toStudyRoomTimeSlots(studyRoomId: UUID) =
        timeSlotIds.map {
            StudyRoomTimeSlot(
                studyRoomId = studyRoomId,
                timeSlotId = it
            )
        }
}
