package team.aliens.dms.domain.chatbot.service

import org.springframework.transaction.annotation.Transactional
import team.aliens.dms.common.annotation.Service
import team.aliens.dms.domain.chatbot.model.ChatbotDocument
import team.aliens.dms.domain.chatbot.model.ChatbotDocumentChunk
import team.aliens.dms.domain.chatbot.spi.CommandChatbotDocumentChunkPort
import team.aliens.dms.domain.chatbot.spi.CommandChatbotDocumentPort
import team.aliens.dms.domain.chatbot.spi.EmbeddingPort
import team.aliens.dms.domain.chatbot.spi.QueryChatbotDocumentPort
import java.time.LocalDateTime
import java.util.UUID

/**
 * 옛 청크 삭제 → 문서 저장 → 새 청크 저장이 한 트랜잭션이어야 해서 클래스에 건다.
 * 임베딩 API 호출도 그 안에 들어가지만, 인제스천은 매니저가 파일을 올릴 때만 도는 드문 작업이라 감수한다.
 */
@Service
@Transactional
class CommandChatbotServiceImpl(
    private val embeddingPort: EmbeddingPort,
    private val chunker: ChatbotDocumentChunker,
    private val queryChatbotDocumentPort: QueryChatbotDocumentPort,
    private val commandChatbotDocumentPort: CommandChatbotDocumentPort,
    private val commandChatbotDocumentChunkPort: CommandChatbotDocumentChunkPort
) : CommandChatbotService {

    override fun ingestChatbotDocument(
        schoolId: UUID,
        fileName: String,
        content: String,
        publishedAt: LocalDateTime
    ): ChatbotDocument {
        val now = LocalDateTime.now()
        val title = ChatbotDocument.titleOf(fileName, content)
        val contentHash = ChatbotDocument.contentHashOf(content)
        val embeddingModel = embeddingPort.embeddingModel()

        // 기존에 값이 존재하면 null
        val existing = queryChatbotDocumentPort.queryChatbotDocumentBySchoolIdAndSourceUri(schoolId, fileName)

        // 같은 파일을 같은 모델로 이미 올렸으면 할 일이 없음
        if (existing != null && existing.isUpToDate(contentHash, embeddingModel)) {
            return existing
        }

        val document = existing?.copy(
            title = title,
            contentHash = contentHash,
            embeddingModel = embeddingModel,
            updatedAt = now,
            publishedAt = publishedAt
        ) ?: ChatbotDocument(
            title = title,
            sourceUri = fileName,
            contentHash = contentHash,
            embeddingModel = embeddingModel,
            createdAt = now,
            updatedAt = now,
            publishedAt = publishedAt,
            schoolId = schoolId
        )

        val sections = chunker.chunk(title, content)
        val embeddings = embeddingPort.embedAll(sections.map { it.textForEmbedding() })

        // 기존에 DB에 있던 청크들을 삭제함
        if (existing != null) {
            commandChatbotDocumentChunkPort.deleteChatbotDocumentChunksByDocumentId(existing.id)
        }

        // 새 문서는 저장 시점에 id가 생기므로 반환값을 써야 한다. document.id 는 UUID(0, 0) 그대로다
        val savedDocument = commandChatbotDocumentPort.saveChatbotDocument(document)
        commandChatbotDocumentChunkPort.saveAllChatbotDocumentChunks(
            sections.zip(embeddings) { section, embedding ->
                ChatbotDocumentChunk(
                    documentId = savedDocument.id,
                    orderIndex = section.orderIndex,
                    sectionPath = section.sectionPath,
                    content = section.content,
                    embedding = embedding,
                    createdAt = now,
                    schoolId = savedDocument.schoolId
                )
            }
        )

        return savedDocument
    }

    override fun deleteChatbotDocument(document: ChatbotDocument) {
        // 청크 행은 FK ON DELETE CASCADE 로 문서와 함께 지워진다
        commandChatbotDocumentPort.deleteChatbotDocument(document)
    }
}
