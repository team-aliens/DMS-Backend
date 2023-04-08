package team.aliens.dms.domain.student.model

import team.aliens.dms.common.annotation.Aggregate
import java.util.UUID

@Aggregate
data class VerifiedStudent(

    val id: UUID = UUID.randomUUID(),

    val schoolName: String,

    val name: String,

    val roomNumber: String,

    val roomLocation: String,

    val gcn: String,

    val sex: Sex

) {

    fun calculateEachGcn(): Triple<Int, Int, Int> {
        val classRoomIndex: Int = if (gcn.length == 4) 2 else 3
        val number = gcn.substring(classRoomIndex).toInt()
        val classRoom = gcn.substring(1, classRoomIndex).toInt()
        val grade = gcn.substring(0, 1).toInt()

        return Triple(grade, classRoom, number)
    }
}
