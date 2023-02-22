package team.aliens.dms.domain.point.model

import team.aliens.dms.common.annotation.Aggregate
import team.aliens.dms.domain.point.exception.PointHistoryCanNotCancelException
import java.time.LocalDateTime
import java.util.UUID

@Aggregate
data class PointHistory(

    val id: UUID = UUID(0, 0),

    val studentName: String,

    val studentGcn: String,

    val bonusTotal: Int,

    val minusTotal: Int,

    val isCancel: Boolean,

    val pointName: String,

    val pointScore: Int,

    val pointType: PointType,

    val createdAt: LocalDateTime,

    val schoolId: UUID

) {
    fun cancelHistory(): PointHistory {

        if (this.isCancel) {
            throw PointHistoryCanNotCancelException
        }

        val (bonusTotal, minusTotal) = calculateCanceledPointTotal()

        return PointHistory(
            isCancel = true,
            studentName = this.studentName,
            studentGcn = this.studentGcn,
            bonusTotal = bonusTotal,
            minusTotal = minusTotal,
            pointName = this.pointName,
            pointScore = this.pointScore,
            pointType = this.pointType,
            createdAt = LocalDateTime.now(),
            schoolId = this.schoolId
        )
    }

    private fun calculateCanceledPointTotal(): Pair<Int, Int> {
        return if (this.pointType == PointType.BONUS) {
            Pair(this.bonusTotal - this.pointScore, this.minusTotal)
        } else {
            Pair(this.bonusTotal, this.minusTotal - pointScore)
        }
    }
}