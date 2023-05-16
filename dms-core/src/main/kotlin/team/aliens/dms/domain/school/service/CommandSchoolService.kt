package team.aliens.dms.domain.school.service

import team.aliens.dms.domain.school.model.School

interface CommandSchoolService {

    fun saveSchool(school: School): School
}
