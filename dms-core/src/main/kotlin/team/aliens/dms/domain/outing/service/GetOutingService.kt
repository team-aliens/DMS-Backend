package team.aliens.dms.domain.outing.service

import team.aliens.dms.domain.outing.model.OutingType
import java.util.UUID

interface GetOutingService {

    fun getOutingType(outingType: OutingType): OutingType

    fun getAllOutingTypeTitlesBySchoolIdAndKeyword(schoolId: UUID, keyword: String?): List<String>
}
