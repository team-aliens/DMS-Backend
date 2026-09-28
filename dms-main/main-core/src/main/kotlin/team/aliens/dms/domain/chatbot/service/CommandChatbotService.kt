package team.aliens.dms.domain.chatbot.service

import team.aliens.dms.domain.chatbot.model.ChatbotDocument
import team.aliens.dms.domain.chatbot.model.ChatbotQueryLog
import java.time.LocalDateTime
import java.util.UUID

interface CommandChatbotService {

    /**
     * 파일 원문을 청킹·임베딩해서 문서와 청크를 저장한다. 임베딩은 청크 행의 vector 컬럼에 그대로 들어가므로
     * 저장이 곧 색인이다. 같은 (schoolId, fileName) 문서가 이미 있으면 옛 청크를 지우고 새 청크로 교체하고,
     * 원문·임베딩 모델까지 같으면 아무것도 하지 않는다(멱등).
     */
    fun ingestChatbotDocument(
        schoolId: UUID,
        fileName: String,
        content: String,
        publishedAt: LocalDateTime
    ): ChatbotDocument

    /** 문서를 지운다. 청크는 FK CASCADE 로 함께 지워진다 */
    fun deleteChatbotDocument(document: ChatbotDocument)

    fun saveChatbotQueryLog(queryLog: ChatbotQueryLog)
}
