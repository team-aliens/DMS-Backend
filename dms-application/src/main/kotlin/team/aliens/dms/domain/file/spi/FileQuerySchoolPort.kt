package team.aliens.dms.domain.file.spi

import team.aliens.dms.domain.school.model.School
import java.util.UUID

interface FileQuerySchoolPort {
    fun querySchoolById(schoolId: UUID): School?
}