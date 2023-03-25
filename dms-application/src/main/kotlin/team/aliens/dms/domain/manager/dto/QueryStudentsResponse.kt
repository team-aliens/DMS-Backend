package team.aliens.dms.domain.manager.dto

import team.aliens.dms.domain.student.model.Sex
import java.util.UUID

data class QueryStudentsResponse(
    val students: List<StudentElement>
) {
    data class StudentElement(
        val id: UUID,
        val name: String,
        val gcn: String,
        val sex: Sex,
        val roomNumber: String,
        val profileImageUrl: String,
        val tags: List<StudentTagElement>
    )

    data class StudentTagElement(
        val name: String,
        val color: String
    )
}
