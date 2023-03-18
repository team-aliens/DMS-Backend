package team.aliens.dms.domain.studyroom

import org.springframework.stereotype.Component
import team.aliens.dms.domain.studyroom.exception.StudyRoomTimeSlotNotFoundException
import team.aliens.dms.domain.studyroom.spi.QueryStudyRoomPort
import java.util.UUID

@Component
class StudyRoomFacade(
    val queryStudyRoomPort: QueryStudyRoomPort
) {
    fun validateNullableTimeSlotId(timeSlotId: UUID?, schoolId: UUID) {
        timeSlotId?.run {
            if (!queryStudyRoomPort.existsTimeSlotById(timeSlotId)) {
                throw StudyRoomTimeSlotNotFoundException
            }
        } ?: run {
            if (queryStudyRoomPort.existsTimeSlotsBySchoolId(schoolId)) {
                throw StudyRoomTimeSlotNotFoundException
            }
        }
    }
}
