package team.aliens.dms.domain.studyroom.dto

import java.util.UUID

data class QuerySeatTypesResponse(
    val types: List<Element>
) {

    data class Element(
        val id: UUID,
        val name: String,
        val color: String
    )
}