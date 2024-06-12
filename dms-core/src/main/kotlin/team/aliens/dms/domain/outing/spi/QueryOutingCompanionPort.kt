package team.aliens.dms.domain.outing.spi

import team.aliens.dms.domain.outing.spi.vo.OutingCompanionDetailsVO
import java.time.LocalDate
import java.util.UUID

interface QueryOutingCompanionPort {

    fun queryOutingCompanionsById(outingApplicationId: UUID): List<OutingCompanionDetailsVO>

    fun existsOutingCompanionById(studentId: UUID): Boolean
}
