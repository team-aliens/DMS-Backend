package team.aliens.dms.domain.chatbot.stub

import team.aliens.dms.domain.chatbot.model.ChatbotDocument
import java.time.LocalDateTime
import java.util.UUID

internal fun createChatbotDocumentStub(
    id: UUID = UUID.randomUUID(),
    title: String = "기숙사 생활 규정",
    sourceUri: String = "dormitory-rules.md",
    contentHash: String = "a".repeat(64),
    embeddingModel: String = "gemini-embedding-001/768",
    createdAt: LocalDateTime = LocalDateTime.now(),
    updatedAt: LocalDateTime = LocalDateTime.now(),
    publishedAt: LocalDateTime = LocalDateTime.now(),
    schoolId: UUID = UUID.randomUUID()
) = ChatbotDocument(
    id = id,
    title = title,
    sourceUri = sourceUri,
    contentHash = contentHash,
    embeddingModel = embeddingModel,
    createdAt = createdAt,
    updatedAt = updatedAt,
    publishedAt = publishedAt,
    schoolId = schoolId
)
