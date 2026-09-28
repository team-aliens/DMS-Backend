package team.aliens.dms.thirdparty.ai.client.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import team.aliens.dms.domain.chatbot.model.TokenUsage

/**
 * 전역 ObjectMapper 가 SNAKE_CASE 전략이라 Gemini 의 camelCase 필드는 @JsonProperty 로 이름을 고정해야 한다.
 * 빠뜨리면 예외 없이 null/0 으로 남는다.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
data class GeminiGenerateContentResponse(
    val candidates: List<Candidate> = emptyList(),
    @JsonProperty("usageMetadata")
    val usageMetadata: UsageMetadata? = null
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

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class UsageMetadata(
        @JsonProperty("promptTokenCount")
        val promptTokenCount: Int? = null,
        @JsonProperty("cachedContentTokenCount")
        val cachedContentTokenCount: Int? = null,
        @JsonProperty("thoughtsTokenCount")
        val thoughtsTokenCount: Int? = null,
        @JsonProperty("candidatesTokenCount")
        val candidatesTokenCount: Int? = null,
        @JsonProperty("totalTokenCount")
        val totalTokenCount: Int? = null
    )

    fun firstText(): String? = candidates.firstOrNull()
        ?.content
        ?.parts
        ?.firstOrNull()
        ?.text
        ?.takeIf { it.isNotBlank() }

    fun tokenUsage(): TokenUsage = usageMetadata?.let {
        TokenUsage(
            promptTokens = it.promptTokenCount ?: 0,
            cachedTokens = it.cachedContentTokenCount ?: 0,
            thoughtsTokens = it.thoughtsTokenCount ?: 0,
            candidatesTokens = it.candidatesTokenCount ?: 0,
            totalTokens = it.totalTokenCount ?: 0
        )
    } ?: TokenUsage.EMPTY
}
