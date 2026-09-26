package team.aliens.dms.domain.chatbot.spi

import team.aliens.dms.domain.chatbot.model.RetrievedChunk
import java.util.UUID

interface QueryChatbotDocumentChunkPort {

    // 같은 학교의 청크 중 질문 임베딩과 코사인 거리가 가까운 순으로 limit 개
    fun searchChatbotDocumentChunks(schoolId: UUID, queryEmbedding: FloatArray, limit: Int): List<RetrievedChunk>
}
