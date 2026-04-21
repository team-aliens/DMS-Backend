package team.aliens.dms.domain.teacher.dto.response

import java.util.UUID

data class TeachersResponse(
    val teachers: List<TeacherResponse>
)

data class TeacherResponse(
    val id: UUID,
    val name: String
)
