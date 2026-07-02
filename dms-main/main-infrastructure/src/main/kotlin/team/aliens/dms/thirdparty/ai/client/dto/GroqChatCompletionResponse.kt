package team.aliens.dms.thirdparty.ai.client.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class GroqChatCompletionResponse(
    val choices: List<Choice> = emptyList()
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Choice(
        val message: Message? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Message(
        val role: String = "",
        val content: String = ""
    )

    fun firstText(): String? = choices.firstOrNull()
        ?.message
        ?.content
        ?.takeIf { it.isNotBlank() }
}
