package team.aliens.dms.domain.chatbot.model

// Gemini generateContent 응답의 usageMetadata. 값이 없으면 0
data class TokenUsage(
    val promptTokens: Int = 0,
    val cachedTokens: Int = 0,
    val thoughtsTokens: Int = 0,
    val candidatesTokens: Int = 0,
    val totalTokens: Int = 0
) {
    companion object {
        val EMPTY = TokenUsage()
    }
}
