package team.aliens.dms.domain.point.spi.vo

import team.aliens.dms.domain.student.model.Student

data class StudentWithPoint(
    val name: String,
    val grade: Int,
    val classRoom: Int,
    val number: Int,
    val bonusTotal: Int,
    val minusTotal: Int
) {
    val gcn: String = "${this.grade}${this.classRoom}${Student.processNumber(number)}"
}
