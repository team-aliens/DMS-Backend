package team.aliens.dms.domain.school.dto

import java.util.UUID

data class SchoolsResponse(
    val schools: List<SchoolElement>
) {
    data class SchoolElement(
        val id: UUID,
        val name: String,
        val address: String
    )
}
