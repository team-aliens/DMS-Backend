package team.aliens.dms.domain.school.spi

import team.aliens.dms.domain.school.model.School
import java.util.UUID

interface QuerySchoolPort {

    fun queryAllSchools(): List<School>

    fun querySchoolById(schoolId: UUID): School?

    fun querySchoolByCode(code: String): School?
}
