package team.aliens.dms.thirdparty.ai.client.dto

/**
 * Groq(OpenAI 호환) chat/completions 요청 바디.
 * system 역할(역할+규정)과 user 역할(학생 질문)을 messages 배열로 분리해 전달한다.
 */
data class GroqChatCompletionRequest(
    val model: String,
    val messages: List<Message>
) {
    data class Message(
        val role: String,
        val content: String
    )

    companion object {
        fun of(model: String, systemInstruction: String, question: String) = GroqChatCompletionRequest(
            model = model,
            messages = listOf(
                Message(role = "system", content = systemInstruction),
                Message(role = "user", content = question)
            )
        )
    }
}
