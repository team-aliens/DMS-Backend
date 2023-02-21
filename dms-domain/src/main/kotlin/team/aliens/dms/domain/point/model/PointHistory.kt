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
    fun canceledHistory(): PointHistory {

        if (this.isCancel) {
            throw PointHistoryCanNotCancelException
        }

        val bonusTotal = this.bonusTotal -
                (if (pointType == PointType.BONUS) pointScore else 0)

        val minusTotal = this.minusTotal -
                (if (pointType == PointType.MINUS) pointScore else 0)

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
}