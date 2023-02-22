package team.aliens.dms.domain.point.spi

import team.aliens.dms.domain.point.model.PointOption
import java.util.UUID

interface QueryPointOptionPort {
    fun queryPointOptionById(pointOptionId: UUID): PointOption?

    fun queryPointOptionsBySchoolIdAndKeyword(schoolId: UUID, keyword: String?): List<PointOption>
    fun existByNameAndSchoolId(name: String, schoolId: UUID): Boolean
}