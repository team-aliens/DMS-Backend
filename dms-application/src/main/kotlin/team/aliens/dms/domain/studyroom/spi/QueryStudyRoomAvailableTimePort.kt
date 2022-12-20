package team.aliens.dms.domain.studyroom.spi

import java.util.UUID
import team.aliens.dms.domain.studyroom.model.StudyRoomAvailableTime

interface QueryStudyRoomAvailableTimePort {

    fun queryStudyRoomAvailableTimeBySchoolId(schoolId: UUID): StudyRoomAvailableTime?

}