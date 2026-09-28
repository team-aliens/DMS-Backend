package team.aliens.dms.persistence.chatbot.mapper

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import team.aliens.dms.domain.chatbot.model.ChatbotQueryLog
import team.aliens.dms.domain.chatbot.model.TokenUsage
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.chatbot.entity.ChatbotQueryLogJpaEntity
import team.aliens.dms.persistence.school.repository.SchoolJpaRepository
import java.util.UUID

@Component
class ChatbotQueryLogMapper(
    private val schoolRepository: SchoolJpaRepository
) : GenericMapper<ChatbotQueryLog, ChatbotQueryLogJpaEntity> {

    override fun toDomain(entity: ChatbotQueryLogJpaEntity?): ChatbotQueryLog? {
        return entity?.let {
            ChatbotQueryLog(
                id = it.id!!,
                question = it.question,
                status = it.status,
                answer = it.answer,
                retrievedChunkIds = splitChunkIds(it.retrievedChunkIds),
                usage = TokenUsage(
                    promptTokens = it.promptTokens,
                    cachedTokens = it.cachedTokens,
                    thoughtsTokens = it.thoughtsTokens,
                    candidatesTokens = it.candidatesTokens,
                    totalTokens = it.totalTokens
                ),
                responseTimeMs = it.responseTimeMs,
                createdAt = it.createdAt,
                schoolId = it.school!!.id!!
            )
        }
    }

    override fun toEntity(domain: ChatbotQueryLog): ChatbotQueryLogJpaEntity {
        val school = schoolRepository.findByIdOrNull(domain.schoolId)

        return ChatbotQueryLogJpaEntity(
            id = domain.id,
            school = school,
            question = domain.question,
            status = domain.status,
            answer = domain.answer,
            retrievedChunkIds = domain.retrievedChunkIds.joinToString(CHUNK_ID_SEPARATOR),
            promptTokens = domain.usage.promptTokens,
            cachedTokens = domain.usage.cachedTokens,
            thoughtsTokens = domain.usage.thoughtsTokens,
            candidatesTokens = domain.usage.candidatesTokens,
            totalTokens = domain.usage.totalTokens,
            responseTimeMs = domain.responseTimeMs,
            createdAt = domain.createdAt
        )
    }

    // 빈 문자열을 split 하면 [""] 가 되므로 빈 리스트로 따로 처리한다
    private fun splitChunkIds(chunkIds: String): List<UUID> =
        if (chunkIds.isEmpty()) emptyList()
        else chunkIds.split(CHUNK_ID_SEPARATOR).map(UUID::fromString)

    companion object {
        private const val CHUNK_ID_SEPARATOR = ","
    }
}
