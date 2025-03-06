package team.aliens.dms.domain.student.spi.vo

import java.util.UUID

data class ModelStudentVO(
    val id: UUID,
    val studentGcn: String,
    val studentName: String,
    val studentProfile: String?

)
