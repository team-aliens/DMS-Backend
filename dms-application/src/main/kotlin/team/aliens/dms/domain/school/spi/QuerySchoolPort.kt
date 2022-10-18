package team.aliens.dms.domain.school.spi

import team.aliens.dms.domain.school.model.School

interface QuerySchoolPort {

    fun queryAllSchool(): List<School>

}