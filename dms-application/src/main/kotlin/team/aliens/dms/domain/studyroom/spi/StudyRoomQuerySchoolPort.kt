package team.aliens.dms.domain.studyroom.spi

import team.aliens.dms.domain.school.model.School
import java.util.UUID

interface StudyRoomQuerySchoolPort {

    fun querySchoolById(schoolId: UUID): School?
}
