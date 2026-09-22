package team.aliens.dms.domain.chatbot.spi

import team.aliens.dms.domain.chatbot.model.ChatbotDocument

interface CommandChatbotDocumentPort {

    fun saveChatbotDocument(document: ChatbotDocument): ChatbotDocument

    fun deleteChatbotDocument(document: ChatbotDocument)
}
