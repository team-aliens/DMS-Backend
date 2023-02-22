package team.aliens.dms.domain.remain.dto

import java.util.UUID

data class QueryRemainOptionsResponse(
    val selectedOption: String?,
    val remainOptions: List<RemainOptionElement>
) {

    data class RemainOptionElement(
        val id: UUID,
        val title: String,
        val description: String
    )
}