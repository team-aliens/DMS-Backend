package team.aliens.dms.domain.chatbot.exception

import team.aliens.dms.common.error.ErrorProperty
import team.aliens.dms.common.error.ErrorStatus

enum class ChatbotErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    CHATBOT_DOCUMENT_INVALID_EXTENSION(ErrorStatus.BAD_REQUEST, "Allowed Extension : md, txt", 1),
    CHATBOT_DOCUMENT_FILE_NAME_TOO_LONG(ErrorStatus.BAD_REQUEST, "Chatbot Document File Name Too Long", 2),

    CHATBOT_DOCUMENT_SCHOOL_MISMATCH(ErrorStatus.FORBIDDEN, "Chatbot Document School Mismatch", 1),

    CHATBOT_DOCUMENT_NOT_FOUND(ErrorStatus.NOT_FOUND, "Chatbot Document Not Found", 1),

    REGULATION_DOCUMENT_NOT_FOUND(ErrorStatus.INTERNAL_SERVER_ERROR, "Regulation Document Not Found", 1),
    CHATBOT_ANSWER_GENERATION_FAILED(ErrorStatus.INTERNAL_SERVER_ERROR, "Chatbot Answer Generation Failed", 2),
    CHATBOT_EMBEDDING_FAILED(ErrorStatus.INTERNAL_SERVER_ERROR, "Chatbot Embedding Failed", 3)
    ;

    override fun status() = status
    override fun message() = message
    override fun code(): String = "CHATBOT-$status-$sequence"
}
