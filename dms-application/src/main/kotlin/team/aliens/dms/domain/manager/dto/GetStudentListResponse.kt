package team.aliens.dms.domain.manager.dto

import java.util.UUID

data class GetStudentListResponse(
    val students: List<StudentElement>
) {
    data class StudentElement(
        val id: UUID,
        val name: String,
        val gcn: String,
        val roomNumber: Int,
        val profileImageUrl: String
    )
}