package team.aliens.dms.domain.student.spi

import team.aliens.dms.domain.school.model.School
import java.util.UUID

interface StudentQuerySchoolPort {

    fun querySchoolByCode(code: String): School?

    fun querySchoolById(schoolId: UUID): School?

}