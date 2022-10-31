package team.aliens.dms.domain.student.dto

import java.util.UUID

data class CheckStudentGcnRequest(
    val schoolId: UUID,
    val grade: Int,
    val classRoom: Int,
    val number: Int
)
