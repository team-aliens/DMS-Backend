package team.aliens.dms.domain.chatbot.spi

interface ChatAiPort {

    fun generateAnswer(systemInstruction: String, question: String): String
}
