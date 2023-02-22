package team.aliens.dms.domain.point.spi

import team.aliens.dms.domain.school.model.School
import java.util.UUID

interface PointQuerySchoolPort {
    fun querySchoolById(schoolId: UUID): School?
}