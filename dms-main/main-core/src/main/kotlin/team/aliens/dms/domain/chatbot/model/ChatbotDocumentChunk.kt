package team.aliens.dms.domain.chatbot.model

import team.aliens.dms.common.annotation.Aggregate
import team.aliens.dms.common.model.SchoolIdDomain
import java.time.LocalDateTime
import java.util.UUID

@Aggregate
data class ChatbotDocumentChunk(

    val id: UUID = UUID(0, 0),

    val documentId: UUID,

    val orderIndex: Int,

    val sectionPath: String,

    val content: String,

    val embedding: FloatArray,

    val createdAt: LocalDateTime,

    override val schoolId: UUID

) : SchoolIdDomain
