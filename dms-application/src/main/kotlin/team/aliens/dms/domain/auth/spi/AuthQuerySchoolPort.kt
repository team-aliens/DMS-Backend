package team.aliens.dms.domain.auth.spi

import team.aliens.dms.domain.school.model.AvailableFeature
import java.util.UUID

interface AuthQuerySchoolPort {

    fun queryAvailableFeaturesBySchoolId(schoolId: UUID): AvailableFeature?

}
