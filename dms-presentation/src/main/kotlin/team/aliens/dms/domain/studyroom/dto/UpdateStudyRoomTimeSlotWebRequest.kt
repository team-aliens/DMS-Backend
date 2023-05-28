package team.aliens.dms.domain.studyroom.dto

import java.util.UUID

data class UpdateStudyRoomTimeSlotWebRequest(
    val timeSlotIds: List<UUID>
)
