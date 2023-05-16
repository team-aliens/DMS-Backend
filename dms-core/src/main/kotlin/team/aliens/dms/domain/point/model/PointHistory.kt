package team.aliens.dms.domain.point.model

import team.aliens.dms.common.annotation.Aggregate
import team.aliens.dms.common.model.SchoolIdDomain
import team.aliens.dms.domain.point.dto.PointRequestType
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

    override val schoolId: UUID

) : SchoolIdDomain {

    companion object {
        fun getTotalPoint(type: PointRequestType, bonusTotal: Int, minusTotal: Int): Int {
            val totalPoint = when (type) {
                PointRequestType.BONUS -> bonusTotal
                PointRequestType.MINUS -> minusTotal
                PointRequestType.ALL -> bonusTotal - minusTotal
            }
            return totalPoint
        }
    }

    fun cancelHistory(pointTotal: Pair<Int, Int>): PointHistory {

        if (this.isCancel) {
            throw PointHistoryCanNotCancelException
        }

        val (bonusTotal, minusTotal) = calculateCanceledPointTotal(pointTotal)

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

    private fun calculateCanceledPointTotal(pointTotal: Pair<Int, Int>): Pair<Int, Int> {
        return if (this.pointType == PointType.BONUS) {
            Pair(pointTotal.first - this.pointScore, pointTotal.second)
        } else {
            Pair(pointTotal.first, pointTotal.second - this.pointScore)
        }
    }
}
