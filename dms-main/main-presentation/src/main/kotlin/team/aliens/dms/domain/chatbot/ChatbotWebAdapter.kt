package team.aliens.dms.domain.chatbot

import jakarta.validation.Valid
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import team.aliens.dms.domain.chatbot.dto.ChatbotAnswerResponse
import team.aliens.dms.domain.chatbot.dto.request.AskChatbotWebRequest
import team.aliens.dms.domain.chatbot.usecase.AskChatbotUseCase

@Validated
@RequestMapping("/chatbots")
@RestController
class ChatbotWebAdapter(
    private val askChatbotUseCase: AskChatbotUseCase
) {

    @PostMapping("/questions")
    fun askQuestion(@RequestBody @Valid request: AskChatbotWebRequest): ChatbotAnswerResponse {
        return askChatbotUseCase.execute(request.question)
    }
}
