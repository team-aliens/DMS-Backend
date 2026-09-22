package team.aliens.dms.domain.chatbot.exception

import team.aliens.dms.common.error.DmsException

object ChatbotDocumentInvalidExtensionException : DmsException(
    ChatbotErrorCode.CHATBOT_DOCUMENT_INVALID_EXTENSION
)

object ChatbotDocumentSchoolMismatchException : DmsException(
    ChatbotErrorCode.CHATBOT_DOCUMENT_SCHOOL_MISMATCH
)

object ChatbotDocumentNotFoundException : DmsException(
    ChatbotErrorCode.CHATBOT_DOCUMENT_NOT_FOUND
)

object RegulationDocumentNotFoundException : DmsException(
    ChatbotErrorCode.REGULATION_DOCUMENT_NOT_FOUND
)

object ChatbotAnswerGenerationFailedException : DmsException(
    ChatbotErrorCode.CHATBOT_ANSWER_GENERATION_FAILED
)

object ChatbotEmbeddingFailedException : DmsException(
    ChatbotErrorCode.CHATBOT_EMBEDDING_FAILED
)
