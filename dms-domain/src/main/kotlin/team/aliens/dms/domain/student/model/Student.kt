package team.aliens.dms.domain.student.model

import team.aliens.dms.common.annotation.Aggregate
import java.util.UUID

@Aggregate
data class Student(

    val studentId: UUID,

    val roomNumber: Int,

    val schoolId: UUID,

    val grade: Int,

    val classRoom: Int,

    val number: Int

) {

    val gcn: String = "${this.grade}${this.classRoom}${processedNumber()}"

    private fun processedNumber() = if (number < 10) "0${number}".toInt() else number
}