package team.aliens.dms.domain.school.service

import team.aliens.dms.domain.school.model.AvailableFeature
import team.aliens.dms.domain.school.model.School
import java.util.UUID

interface GetSchoolService {
    fun queryAllSchools(): List<School>

    fun getAvailableFeaturesBySchoolId(schoolId: UUID): AvailableFeature
}
