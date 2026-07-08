package team.aliens.dms.domain.chatbot.usecase

import team.aliens.dms.common.annotation.ReadOnlyUseCase
import team.aliens.dms.domain.chatbot.dto.ChatbotAnswerResponse
import team.aliens.dms.domain.chatbot.service.ChatbotService

@ReadOnlyUseCase
class AskChatbotUseCase(
    private val chatbotService: ChatbotService
) {

    fun execute(question: String): ChatbotAnswerResponse {
        val answer = chatbotService.generateAnswer(question)
        return ChatbotAnswerResponse(answer)
    }
}
