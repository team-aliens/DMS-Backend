package team.aliens.dms.domain.remain.spi

import team.aliens.dms.domain.remain.model.RemainOption
import java.util.UUID

interface QueryRemainOptionPort {

    fun queryRemainOptionById(remainOptionId: UUID): RemainOption?

    fun queryAllRemainOptionBySchoolId(schoolId: UUID): List<RemainOption>

}