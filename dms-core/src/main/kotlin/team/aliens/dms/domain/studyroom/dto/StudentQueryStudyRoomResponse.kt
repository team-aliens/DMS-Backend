package team.aliens.dms.domain.studyroom.dto

import team.aliens.dms.domain.studyroom.model.StudyRoom
import team.aliens.dms.domain.studyroom.model.TimeSlot
import team.aliens.dms.domain.studyroom.spi.vo.SeatApplicationVO
import java.util.UUID

data class StudentQueryStudyRoomResponse(
    val studyRoom: StudyRoom,
    val timeSlot: TimeSlot,
    val seats: List<SeatApplicationVO>,
    val studentId: UUID
)
