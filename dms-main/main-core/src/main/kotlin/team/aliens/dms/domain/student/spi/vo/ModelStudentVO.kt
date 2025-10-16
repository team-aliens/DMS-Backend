package team.aliens.dms.domain.student.spi.vo

import java.util.UUID

data class ModelStudentVO(
    val id: UUID,
    val gcn: String,
    val name: String,
    val profileImageUrl: String?
)
