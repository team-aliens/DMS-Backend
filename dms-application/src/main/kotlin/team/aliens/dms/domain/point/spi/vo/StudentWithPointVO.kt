package team.aliens.dms.domain.point.spi.vo

import team.aliens.dms.domain.point.model.PointType
import team.aliens.dms.domain.student.model.Student

data class StudentWithPointVO(
    val name: String,
    val grade: Int,
    val classRoom: Int,
    val number: Int,
    val bonusTotal: Int,
    val minusTotal: Int
) {
    val gcn: String = "${this.grade}${this.classRoom}${Student.processNumber(number)}"

    fun calculateUpdatedPointTotal(type: PointType, score: Int): Pair<Int, Int> {
        return if (type == PointType.BONUS) {
            Pair(this.bonusTotal + score, this.minusTotal)
        } else {
            Pair(this.bonusTotal, this.minusTotal + score)
        }
    }
}
