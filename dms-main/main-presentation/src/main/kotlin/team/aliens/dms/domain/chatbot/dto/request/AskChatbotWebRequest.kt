package team.aliens.dms.domain.chatbot.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class AskChatbotWebRequest(

    @field:NotBlank
    @field:Size(max = 500)
    val question: String

)
