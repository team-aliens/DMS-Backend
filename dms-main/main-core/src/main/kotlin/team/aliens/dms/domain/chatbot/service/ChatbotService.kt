package team.aliens.dms.domain.chatbot.service

import team.aliens.dms.common.annotation.Service

@Service
class ChatbotService(
    getChatbotService: GetChatbotService,
    commandChatbotService: CommandChatbotService
) : GetChatbotService by getChatbotService,
    CommandChatbotService by commandChatbotService
