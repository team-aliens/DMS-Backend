package team.aliens.dms.domain.studyroom.spi

import java.util.UUID
import team.aliens.dms.domain.school.model.School

interface StudyRoomAvailableTimeQuerySchoolPort {

    fun querySchoolById(schoolId: UUID): School?

}