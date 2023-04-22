package team.aliens.dms.domain.school.service

import team.aliens.dms.domain.school.model.AvailableFeature
import team.aliens.dms.domain.school.model.School
import java.util.UUID

interface GetSchoolService {
    fun getAllSchools(): List<School>

    fun getSchoolById(schoolId: UUID): School

    fun getSchoolByCode(code: String): School

    fun getAvailableFeaturesBySchoolId(schoolId: UUID): AvailableFeature
}
