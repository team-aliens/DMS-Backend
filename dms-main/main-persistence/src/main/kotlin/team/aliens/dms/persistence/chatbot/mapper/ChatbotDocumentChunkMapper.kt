package team.aliens.dms.persistence.chatbot.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.chatbot.model.ChatbotDocumentChunk
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.chatbot.entity.ChatbotDocumentChunkJpaEntity
import team.aliens.dms.persistence.chatbot.repository.ChatbotDocumentJpaRepository
import team.aliens.dms.persistence.school.repository.SchoolJpaRepository

@Component
class ChatbotDocumentChunkMapper(
    private val chatbotDocumentRepository: ChatbotDocumentJpaRepository,
    private val schoolRepository: SchoolJpaRepository
) : GenericMapper<ChatbotDocumentChunk, ChatbotDocumentChunkJpaEntity> {

    override fun toDomain(entity: ChatbotDocumentChunkJpaEntity?): ChatbotDocumentChunk? {
        return entity?.let {
            ChatbotDocumentChunk(
                id = it.id!!,
                documentId = it.document!!.id!!,
                orderIndex = it.orderIndex,
                sectionPath = it.sectionPath,
                content = it.content,
                embedding = it.embedding,
                createdAt = it.createdAt,
                schoolId = it.school!!.id!!
            )
        }
    }

    override fun toEntity(domain: ChatbotDocumentChunk): ChatbotDocumentChunkJpaEntity {
        val document = chatbotDocumentRepository.findByIdOrNull(domain.documentId)
        val school = schoolRepository.findByIdOrNull(domain.schoolId)

        return ChatbotDocumentChunkJpaEntity(
            id = domain.id,
            document = document,
            school = school,
            orderIndex = domain.orderIndex,
            sectionPath = domain.sectionPath,
            content = domain.content,
            embedding = domain.embedding,
            createdAt = domain.createdAt
        )
    }
}
