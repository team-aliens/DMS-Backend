package team.aliens.dms.domain.chatbot.service

import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.chatbot.exception.ChatbotDocumentNotFoundException
import team.aliens.dms.domain.chatbot.model.ChatbotAnswer
import team.aliens.dms.domain.chatbot.model.ChatbotDocument
import team.aliens.dms.domain.chatbot.model.ChatbotQueryStatus
import team.aliens.dms.domain.chatbot.model.RetrievedChunk
import team.aliens.dms.domain.chatbot.model.TokenUsage
import team.aliens.dms.domain.chatbot.spi.ChatAiPort
import team.aliens.dms.domain.chatbot.spi.EmbeddingPort
import team.aliens.dms.domain.chatbot.spi.QueryChatbotDocumentChunkPort
import team.aliens.dms.domain.chatbot.spi.QueryChatbotDocumentPort
import java.util.UUID

@Service
class GetChatbotServiceImpl(
    private val chatAiPort: ChatAiPort,
    private val embeddingPort: EmbeddingPort,
    private val queryChatbotDocumentChunkPort: QueryChatbotDocumentChunkPort,
    private val queryChatbotDocumentPort: QueryChatbotDocumentPort
) : GetChatbotService {

    // 질문과 가까운 청크만 검색해서 근거로 넣는다
    override fun generateAnswer(schoolId: UUID, question: String): ChatbotAnswer {
        val queryEmbedding = embeddingPort.embedQuery(question)
        val chunks = queryChatbotDocumentChunkPort.searchChatbotDocumentChunks(schoolId, queryEmbedding, SEARCH_LIMIT)

        // 근거가 하나도 없으면 LLM 을 부르지 않고 안내 문구로 답한다
        if (chunks.isEmpty()) {
            return ChatbotAnswer(
                answer = NO_CONTEXT_ANSWER,
                status = ChatbotQueryStatus.NO_CONTEXT,
                retrievedChunkIds = emptyList(),
                usage = TokenUsage.EMPTY
            )
        }

        val result = chatAiPort.generateAnswer(buildSystemInstruction(chunks), question)
        return ChatbotAnswer(
            answer = result.text,
            status = ChatbotQueryStatus.ANSWERED,
            retrievedChunkIds = chunks.map { it.chunkId },
            usage = result.usage
        )
    }

    override fun getChatbotDocumentById(documentId: UUID): ChatbotDocument {
        return queryChatbotDocumentPort.queryChatbotDocumentById(documentId)
            ?: throw ChatbotDocumentNotFoundException
    }

    // 검색 순서(가까운 순) 그대로 이어 붙인다. trimIndent 는 끼워 넣은 여러 줄 본문 때문에 동작하지 않으므로 문자열을 직접 잇는다
    private fun buildSystemInstruction(chunks: List<RetrievedChunk>): String {
        val excerpts = chunks.joinToString("\n\n") { "### ${it.documentTitle} > ${it.sectionPath}\n${it.content}" }
        return "$ROLE_INSTRUCTION\n\n$EXCERPT_HEADER\n$excerpts"
    }

    companion object {
        private const val SEARCH_LIMIT = 5
        private const val EXCERPT_HEADER = "[기숙사 규정 발췌]"
        private const val NO_CONTEXT_ANSWER = "규정에서 확인되지 않는 내용입니다. 자세한 사항은 사감선생님께 문의해 주세요."

        private val ROLE_INSTRUCTION = """
            너는 기숙사 생활을 안내하는 도우미야.
            반드시 아래 제공된 기숙사 규정에만 근거해서 한국어로 답변해.
            규정에서 근거를 찾을 수 없는 질문이면 내용을 지어내지 말고,
            "$NO_CONTEXT_ANSWER" 라고만 답해.
            답변은 학생이 이해하기 쉽게 간결하게 작성해.
        """.trimIndent()
    }
}
