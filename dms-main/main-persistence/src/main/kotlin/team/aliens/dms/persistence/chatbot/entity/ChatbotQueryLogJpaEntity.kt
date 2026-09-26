package team.aliens.dms.persistence.chatbot.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import team.aliens.dms.domain.chatbot.model.ChatbotQueryStatus
import team.aliens.dms.persistence.BaseEntity
import team.aliens.dms.persistence.school.entity.SchoolJpaEntity
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "tbl_chatbot_query_log")
class ChatbotQueryLogJpaEntity(

    id: UUID?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    val school: SchoolJpaEntity?,

    @Column(columnDefinition = "TEXT", nullable = false)
    val question: String,

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    val status: ChatbotQueryStatus,

    @Column(columnDefinition = "TEXT")
    val answer: String?,

    // 쉼표로 이은 청크 id. 청크는 재업로드 때 교체되므로 FK 를 걸지 않는다
    @Column(columnDefinition = "TEXT", nullable = false)
    val retrievedChunkIds: String,

    @Column(nullable = false)
    val promptTokens: Int,

    @Column(nullable = false)
    val cachedTokens: Int,

    @Column(nullable = false)
    val thoughtsTokens: Int,

    @Column(nullable = false)
    val candidatesTokens: Int,

    @Column(nullable = false)
    val totalTokens: Int,

    @Column(nullable = false)
    val responseTimeMs: Long,

    override val createdAt: LocalDateTime

) : BaseEntity(id)
