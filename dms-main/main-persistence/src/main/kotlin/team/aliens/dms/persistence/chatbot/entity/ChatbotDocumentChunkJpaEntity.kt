package team.aliens.dms.persistence.chatbot.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.hibernate.annotations.Array
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import team.aliens.dms.persistence.BaseEntity
import team.aliens.dms.persistence.school.entity.SchoolJpaEntity
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "tbl_chatbot_document_chunk")
class ChatbotDocumentChunkJpaEntity(

    id: UUID?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    val document: ChatbotDocumentJpaEntity?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    val school: SchoolJpaEntity?,

    @Column(nullable = false)
    val orderIndex: Int,

    @Column(length = 300, nullable = false)
    val sectionPath: String,

    @Column(columnDefinition = "TEXT", nullable = false)
    val content: String,

    /**
     * pgvector 의 vector(768) 컬럼. 저장 전에 L2 정규화돼 있어서 코사인 거리가 내적과 같다.
     * 차원을 바꾸면 마이그레이션(컬럼 재생성)과 chatbot.embedding.dimension 설정을 함께 고쳐야 한다.
     */
    @JdbcTypeCode(SqlTypes.VECTOR)
    @Array(length = EMBEDDING_DIMENSION)
    @Column(nullable = false)
    val embedding: FloatArray,

    override val createdAt: LocalDateTime

) : BaseEntity(id) {

    companion object {
        const val EMBEDDING_DIMENSION = 768
    }
}
