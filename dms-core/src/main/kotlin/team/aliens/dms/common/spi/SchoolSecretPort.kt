package team.aliens.dms.common.spi

import java.util.UUID
import team.aliens.dms.common.model.SchoolSecret

interface SchoolSecretPort {

    fun saveSchoolSecret(schoolSecret: SchoolSecret)

    fun querySchoolSecretBySchoolId(schoolId: UUID): SchoolSecret?
}
