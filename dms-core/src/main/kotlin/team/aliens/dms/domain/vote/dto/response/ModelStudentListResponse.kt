package team.aliens.dms.domain.vote.dto.response

import java.util.UUID

data class ModelStudentListResponse(
    val id: UUID,
    val studentGcn: String,
    val studentName: String,
    val studentProfile: String?
)
