package team.aliens.dms.domain.chatbot.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.chatbot.spi.ChatAiPort
import team.aliens.dms.domain.chatbot.spi.RegulationDocumentPort

@Service
class ChatbotServiceImpl(
    private val regulationDocumentPort: RegulationDocumentPort,
    private val chatAiPort: ChatAiPort
) : ChatbotService {

    override fun generateAnswer(question: String): String {
        val regulation = regulationDocumentPort.loadRegulation()
        val systemInstruction = buildSystemInstruction(regulation)
        return chatAiPort.generateAnswer(systemInstruction, question)
    }

    private fun buildSystemInstruction(regulation: String): String {
        return """
            $ROLE_INSTRUCTION

            [기숙사 규정 문서]
            $regulation
        """.trimIndent()
    }

    companion object {
        private val ROLE_INSTRUCTION = """
            너는 기숙사 생활을 안내하는 도우미야.
            반드시 아래 제공된 "기숙사 규정 문서"에만 근거해서 한국어로 답변해.
            규정 문서에서 근거를 찾을 수 없는 질문이면 내용을 지어내지 말고,
            "규정에서 확인되지 않는 내용입니다. 자세한 사항은 사감선생님께 문의해 주세요." 라고만 답해.
            답변은 학생이 이해하기 쉽게 간결하게 작성해.
        """.trimIndent()
    }
}
