package team.aliens.dms.domain.chatbot.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import team.aliens.dms.domain.chatbot.model.ChatbotAnswerMode

data class AskChatbotWebRequest(

    @field:NotBlank
    @field:Size(max = 500)
    val question: String,

    // STUFFING/RAG 토큰 사용량 측정·비교용. 머지 전에 제거한다
    val mode: ChatbotAnswerMode = ChatbotAnswerMode.RAG

)
