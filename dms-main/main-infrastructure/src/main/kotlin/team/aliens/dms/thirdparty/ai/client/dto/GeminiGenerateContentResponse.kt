package team.aliens.dms.thirdparty.ai.client.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class GeminiGenerateContentResponse(
    val candidates: List<Candidate> = emptyList()
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Candidate(
        val content: Content? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Content(
        val parts: List<Part> = emptyList()
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Part(
        val text: String = ""
    )

    fun firstText(): String? = candidates.firstOrNull()
        ?.content
        ?.parts
        ?.firstOrNull()
        ?.text
        ?.takeIf { it.isNotBlank() }
}
