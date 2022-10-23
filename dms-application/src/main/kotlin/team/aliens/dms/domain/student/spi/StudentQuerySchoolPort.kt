package team.aliens.dms.domain.student.spi

import team.aliens.dms.domain.school.model.School

interface StudentQuerySchoolPort {

    fun querySchoolByCode(code: String): School?

}