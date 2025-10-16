package team.aliens.dms.domain.point.spi.vo

import java.util.UUID

open class StudentTotalVO(
    val studentId: UUID,
    val bonusTotal: Int,
    val minusTotal: Int
)
