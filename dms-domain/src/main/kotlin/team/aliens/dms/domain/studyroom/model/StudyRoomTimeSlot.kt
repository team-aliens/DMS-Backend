package team.aliens.dms.domain.studyroom.model

import java.util.UUID

data class StudyRoomTimeSlot(

    val id: UUID = UUID(0, 0),

    val schoolId: UUID,

    val name: String
)
