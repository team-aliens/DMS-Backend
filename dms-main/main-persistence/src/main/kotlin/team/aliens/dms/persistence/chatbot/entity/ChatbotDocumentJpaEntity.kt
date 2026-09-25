package team.aliens.dms.persistence.chatbot.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import team.aliens.dms.persistence.BaseEntity
import team.aliens.dms.persistence.school.entity.SchoolJpaEntity
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "tbl_chatbot_document")
class ChatbotDocumentJpaEntity(

    id: UUID?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    val school: SchoolJpaEntity?,

    @Column(length = 100, nullable = false)
    val title: String,

    @Column(length = 500, nullable = false)
    val sourceUri: String,

    @Column(length = 64, nullable = false)
    val contentHash: String,

    @Column(length = 50, nullable = false)
    val embeddingModel: String,

    override val createdAt: LocalDateTime,

    @Column(nullable = false)
    val updatedAt: LocalDateTime,

    @Column(nullable = false)
    val publishedAt: LocalDateTime

) : BaseEntity(id)
