package team.aliens.dms.domain.outing.spi

import team.aliens.dms.domain.outing.model.OutingType
import java.util.UUID

interface QueryOutingTypePort {

    fun existsOutingType(outingType: OutingType): Boolean

    fun queryOutingType(outingType: OutingType): OutingType?

    fun queryAllOutingTypeTitlesBySchoolIdAndKeyword(schoolId: UUID, keyword: String?): List<String>
}
