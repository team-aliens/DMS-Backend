package team.aliens.dms.domain.tag.dto

import java.util.UUID

data class TagResponse(
    val id: UUID,
    val name: String,
    val color: String
)
