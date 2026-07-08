package team.aliens.dms.domain.chatbot.exception

import team.aliens.dms.common.error.DmsException

object RegulationDocumentNotFoundException : DmsException(
    ChatbotErrorCode.REGULATION_DOCUMENT_NOT_FOUND
)

object ChatbotAnswerGenerationFailedException : DmsException(
    ChatbotErrorCode.CHATBOT_ANSWER_GENERATION_FAILED
)
