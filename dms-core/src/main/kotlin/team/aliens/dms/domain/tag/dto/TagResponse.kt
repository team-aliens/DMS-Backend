package team.aliens.dms.domain.tag.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import team.aliens.dms.domain.tag.model.Tag
import java.util.UUID


data class TagResponse @JsonCreator constructor(
    @JsonProperty("id") val id: UUID,
    @JsonProperty("name") val name: String?,
    @JsonProperty("color") val color: String?
) {
    companion object {
        fun of(tag: Tag): TagResponse {
            return TagResponse(
                id = tag.id,
                name = tag.name,
                color = tag.color
            )
        }
    }
}

data class TagsResponse(
    val tags: List<TagResponse>
) {
    companion object {
        fun of(tags: List<Tag>): TagsResponse {
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
