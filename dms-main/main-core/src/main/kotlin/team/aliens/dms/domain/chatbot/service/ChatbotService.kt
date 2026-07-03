package team.aliens.dms.domain.chatbot.service

interface ChatbotService {

    fun generateAnswer(question: String): String
}
