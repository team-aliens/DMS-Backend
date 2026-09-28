package team.aliens.dms.persistence.chatbot.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.chatbot.model.ChatbotDocument
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.chatbot.entity.ChatbotDocumentJpaEntity
import team.aliens.dms.persistence.school.repository.SchoolJpaRepository

@Component
class ChatbotDocumentMapper(
    private val schoolRepository: SchoolJpaRepository
) : GenericMapper<ChatbotDocument, ChatbotDocumentJpaEntity> {

    override fun toDomain(entity: ChatbotDocumentJpaEntity?): ChatbotDocument? {
        return entity?.let {
            ChatbotDocument(
                id = it.id!!,
                title = it.title,
                sourceUri = it.sourceUri,
                contentHash = it.contentHash,
                embeddingModel = it.embeddingModel,
                createdAt = it.createdAt,
                updatedAt = it.updatedAt,
                publishedAt = it.publishedAt,
                schoolId = it.school!!.id!!
            )
        }
    }

    override fun toEntity(domain: ChatbotDocument): ChatbotDocumentJpaEntity {
        val school = schoolRepository.findByIdOrNull(domain.schoolId)

        return ChatbotDocumentJpaEntity(
            id = domain.id,
            school = school,
            title = domain.title,
            sourceUri = domain.sourceUri,
            contentHash = domain.contentHash,
            embeddingModel = domain.embeddingModel,
            createdAt = domain.createdAt,
            updatedAt = domain.updatedAt,
            publishedAt = domain.publishedAt
        )
    }
}
