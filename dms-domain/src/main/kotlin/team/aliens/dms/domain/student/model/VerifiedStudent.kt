package team.aliens.dms.domain.student.model

import team.aliens.dms.common.annotation.Aggregate
import java.util.UUID

@Aggregate
data class VerifiedStudent(

    val id: UUID,

    val schoolName: String,

    val name: String,

    val roomNumber: String,

    val roomLocation: String,

    val gcn: String,

    val sex: Sex

) {

    fun calculateEachGcn(): Triple<Int, Int, Int> {
        var gcn = this.gcn.toInt()

        val number = gcn % 100
        gcn /= 100

        val classRoom = gcn % 10
        gcn /= 10

        val grade = gcn % 10

        return Triple(grade, classRoom, number)
    }
}
