package team.aliens.dms.thirdparty.ai.client.dto

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * Gemini generateContent 요청 바디.
 * system_instruction(역할+규정) 과 contents(학생 질문) 를 분리해 전달한다.
 */
data class GeminiGenerateContentRequest(
    @JsonProperty("system_instruction")
    val systemInstruction: Content,
    val contents: List<Content>
) {
    data class Content(
        val parts: List<Part>
    )

    data class Part(
        val text: String
    )

    companion object {
        fun of(systemInstruction: String, question: String) = GeminiGenerateContentRequest(
            systemInstruction = Content(parts = listOf(Part(systemInstruction))),
            contents = listOf(Content(parts = listOf(Part(question))))
        )
    }
}
