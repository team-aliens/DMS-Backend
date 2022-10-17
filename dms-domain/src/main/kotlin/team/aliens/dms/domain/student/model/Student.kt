package team.aliens.dms.domain.student.model

import team.aliens.dms.global.annotation.Aggregate
import java.util.UUID

@Aggregate
data class Student(

    val studentId: UUID,

    val roomNumber: Int,

    val schoolId: UUID,

    val grade: Int,

    val classRoom: Int,

    val number: Int,
)