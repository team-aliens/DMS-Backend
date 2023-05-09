package team.aliens.dms.domain.tag.dto

import java.util.UUID
import team.aliens.dms.domain.tag.model.Tag

data class TagResponse(
    val id: UUID,
    val name: String?,
    val color: String?
) {
    companion object {
        fun of(tags: List<Tag>): List<TagResponse> {
            return tags.map {
                TagResponse(
                    id = it.id,
                    name = it.name,
                    color = it.color
                )
            }
        }
    }
}

data class TagsResponse(
    val tags: List<TagResponse>
) {
    companion object {
        fun of(tags:List<Tag>): TagsResponse {
            return TagsResponse(
                    tags.map {
                        TagResponse(
                                id = it.id,
                                name = it.name,
                                color = it.color
                        )
                    }
            )
        }
    }
}

data class TagIdResponse(
    val tagId: UUID
)
