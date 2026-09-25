package team.aliens.dms.persistence.chatbot

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.chatbot.model.ChatbotDocument
import team.aliens.dms.domain.chatbot.spi.ChatbotDocumentPort
import team.aliens.dms.persistence.chatbot.mapper.ChatbotDocumentMapper
import team.aliens.dms.persistence.chatbot.repository.ChatbotDocumentJpaRepository
import java.util.UUID

@Component
class ChatbotDocumentPersistenceAdapter(
    private val chatbotDocumentMapper: ChatbotDocumentMapper,
    private val chatbotDocumentRepository: ChatbotDocumentJpaRepository
) : ChatbotDocumentPort {

    override fun queryChatbotDocumentById(documentId: UUID) =
        chatbotDocumentMapper.toDomain(
            chatbotDocumentRepository.findByIdOrNull(documentId)
        )

    override fun queryChatbotDocumentBySchoolIdAndSourceUri(schoolId: UUID, sourceUri: String) =
        chatbotDocumentMapper.toDomain(
            chatbotDocumentRepository.findBySchoolIdAndSourceUri(schoolId, sourceUri)
        )

    override fun saveChatbotDocument(document: ChatbotDocument): ChatbotDocument {
        return chatbotDocumentMapper.toDomain(
            chatbotDocumentRepository.save(chatbotDocumentMapper.toEntity(document))
        )!!
    }

    override fun deleteChatbotDocument(document: ChatbotDocument) {
        chatbotDocumentRepository.deleteById(document.id)
    }
}
