package team.aliens.dms.domain.student.model

import team.aliens.dms.global.annotation.Aggregate
import java.util.UUID

@Aggregate
data class Student(

    val studentId: UUID,

    val grade: Int,

    val classRoom: Int,

    val studentNumber: Int,

    val roomNumber: Int,

    val schoolId: UUID
)