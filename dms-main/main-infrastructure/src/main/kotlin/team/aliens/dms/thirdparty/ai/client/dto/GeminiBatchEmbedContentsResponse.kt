package team.aliens.dms.thirdparty.ai.client.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class GeminiBatchEmbedContentsResponse(
    val embeddings: List<Embedding> = emptyList()
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Embedding(
        val values: List<Float> = emptyList()
    )
}
