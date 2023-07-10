package team.aliens.dms.domain.school.spi

import team.aliens.dms.domain.school.model.ApplicationAvailableTime
import team.aliens.dms.domain.school.model.ApplicationAvailableTimeType
import team.aliens.dms.domain.school.model.AvailableFeature
import team.aliens.dms.domain.school.model.School
import java.util.UUID

interface QuerySchoolPort {

    fun queryAllSchools(): List<School>

    fun querySchoolById(schoolId: UUID): School?

    fun querySchoolByCode(code: String): School?

    fun queryAvailableFeaturesBySchoolId(schoolId: UUID): AvailableFeature?

    fun queryApplicationAvailableTimeBySchoolIdAndType(schoolId: UUID, type: ApplicationAvailableTimeType): ApplicationAvailableTime?
}
