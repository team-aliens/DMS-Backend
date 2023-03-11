package team.aliens.dms.domain.studyroom.model

import java.util.UUID

data class StudyRoomTimeSlot(

    val id: UUID,

    val schoolId: UUID,

    val name: String
)
