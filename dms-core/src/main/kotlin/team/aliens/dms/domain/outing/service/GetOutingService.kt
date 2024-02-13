package team.aliens.dms.domain.outing.service

import team.aliens.dms.domain.outing.model.OutingType
import java.util.UUID

interface GetOutingService {

    fun getOutingType(outingType: OutingType): OutingType

    fun getAllOutingTypeTitlesBySchoolId(schoolId: UUID): List<String>
}
