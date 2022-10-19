package team.aliens.dms.domain.school.spi

import team.aliens.dms.domain.school.model.School
import java.util.UUID

interface QuerySchoolPort {

    fun queryAllSchool(): List<School>

    fun querySchoolById(id: UUID): School?

}