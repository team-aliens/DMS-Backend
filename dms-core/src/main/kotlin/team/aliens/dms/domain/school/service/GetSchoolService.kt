package team.aliens.dms.domain.school.service

import team.aliens.dms.domain.school.model.School

interface GetSchoolService {
    fun queryAllSchools(): List<School>
}
