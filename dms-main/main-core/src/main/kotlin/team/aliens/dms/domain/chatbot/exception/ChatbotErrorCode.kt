package team.aliens.dms.domain.chatbot.exception

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class ChatbotErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    REGULATION_DOCUMENT_NOT_FOUND(ErrorStatus.INTERNAL_SERVER_ERROR, "Regulation Document Not Found", 1),
    CHATBOT_ANSWER_GENERATION_FAILED(ErrorStatus.INTERNAL_SERVER_ERROR, "Chatbot Answer Generation Failed", 2)
    ;

    override fun status() = status
    override fun message() = message
    override fun code(): String = "CHATBOT-$status-$sequence"
}
