package team.aliens.dms.domain.remain.spi

import team.aliens.dms.domain.school.model.School
import java.util.UUID

interface RemainQuerySchoolPort {
    fun querySchoolById(schoolId: UUID): School?
}
